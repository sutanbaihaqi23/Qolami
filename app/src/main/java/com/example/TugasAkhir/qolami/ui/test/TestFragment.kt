package com.example.TugasAkhir.qolami.ui.test

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.QuestionAdapter
import com.example.TugasAkhir.qolami.customview.DrawingView
import com.example.TugasAkhir.qolami.data.TempResultTest
import com.example.TugasAkhir.qolami.databinding.FragmentTestBinding
import com.example.TugasAkhir.qolami.util.ModelUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter


class TestFragment : Fragment() {
    private lateinit var binding: FragmentTestBinding
    private var startQuestion = 0 // Mulai dari 0 agar soal pertama dimulai dari random
    private val totalQuestions = 10
    private lateinit var questionAdapter: QuestionAdapter
    private var timer: CountDownTimer? = null
    private val timePerQuestion: Long = 11000
    private lateinit var tflite: Interpreter
    private lateinit var drawingView: DrawingView
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private val tempResultTests = mutableListOf<TempResultTest>()
    private var remainingTime: Long = timePerQuestion

    private var mediaPlayer: MediaPlayer? = null

    // Mengacak urutan soal dari classLabelsModel
    private var randomQuestions: List<String> = classLabelsModel.toList().shuffled()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestBinding.inflate(inflater, container, false)
        tflite = ModelUtil.loadModel(requireContext(), "model_hijaiyah_baru_dataset_ditambah.tflite")
        drawingView = binding.drawingView
        correctAnswers = 0
        incorrectAnswers = 0

        binding.btnBackToHomeTest.setOnClickListener {
            showQuitExamDialog()
        }
        binding.icSpeaker.setOnClickListener {
            val soundResId = getSoundForHuruf(randomQuestions[startQuestion])
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
            mediaPlayer?.start()

        }
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }

        // Mengatur adapter RecyclerView
        questionAdapter = QuestionAdapter(totalQuestions, startQuestion)
        binding.rvNumberQuestion.layoutManager = GridLayoutManager(
            requireContext(),
            5, // Jumlah item per baris
            GridLayoutManager.VERTICAL,
            false
        )
        binding.rvNumberQuestion.adapter = questionAdapter

        // Tombol untuk submit jawaban
        binding.submitButton.setOnClickListener {
            timer?.cancel()
            checkAnswer() // Cek jawaban saat tombol submit ditekan
        }
        binding.icEraser.setOnClickListener {
            drawingView.clearCanvas()
        }

        startTimer() // Mulai timer saat fragment pertama kali ditampilkan

        // Menampilkan soal pertama setelah tampilan fragment diinisialisasi
        displayQuestion()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle tombol back dari perangkat
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showQuitExamDialog()
            timer?.cancel()
        }
    }

    private fun startTimer() {
        timer?.cancel() // Hentikan timer sebelumnya jika ada

        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished // Simpan waktu tersisa
                binding.writingTestTimer.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                remainingTime = timePerQuestion
                checkAnswer()
            }
        }.start()
    }


    private fun resetTimer() {
        remainingTime = timePerQuestion // Reset waktu ke durasi penuh
    }


    private fun moveToNextQuestion() {
        resetTimer()
        startQuestion++
        if (startQuestion == 5) {
            navigateToTest2Fragment() // Pindah ke Test2Fragment pada soal ke-6
            return
        }
        if (startQuestion < totalQuestions) {
            questionAdapter.updateCurrentQuestion(startQuestion)
            drawingView.clearCanvas()
            displayQuestion()
            startTimer()
        } else {
            // Tes selesai
            timer?.cancel()

        }
    }

    private fun navigateToTest2Fragment() {
        // Siapkan data untuk dikirim
        val bundle = Bundle().apply {
            putInt("correctAnswers", correctAnswers)
            putInt("incorrectAnswers", incorrectAnswers)
            putInt("startQuestion", startQuestion)
            putParcelableArrayList("tempResultTests", ArrayList(tempResultTests))
        }

        // Buat instance Test2Fragment dan tambahkan data ke arguments
        val test2Fragment = Test2Fragment().apply {
            arguments = bundle
        }

        // Ganti fragment menggunakan FragmentTransaction
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, test2Fragment) // `fragment_container` adalah ID container Anda
            .addToBackStack(null) // Tambahkan ke backstack jika ingin kembali ke fragment sebelumnya
            .commit()
    }

    private fun showQuitExamDialog() {
        correctAnswers = 0
        incorrectAnswers = 0
        startQuestion=0
        tempResultTests.clear()
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Keluar Ujian ?")
            .setMessage("Kamu Yakin ingin keluar dari Ujian ?")
            .setPositiveButton("Iya") { dialog, _ ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WritingTestHomeFragment())
                    .addToBackStack(null)
                    .commit()
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
                startTimer() // Jika Anda memiliki timer dan ingin melanjutkannya.
            }
            .create()

        // Membuat dialog tidak dapat dibatalkan dengan cara lain selain menekan tombol Yes/No
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.show()
    }



    private fun displayQuestion() {
        // Menampilkan soal berdasarkan urutan acak dari randomQuestions
        if (startQuestion in randomQuestions.indices) {
            val currentQuestion = randomQuestions[startQuestion]
            binding.letterQuestion.text = currentQuestion
        } else {
            Log.e("TestFragment", "Index out of bounds for startQuestion: $startQuestion")
        }
    }




    private fun checkAnswer() {
        var isCorrect = false

        // Ambil bitmap dari DrawingView
        val bitmap = drawingView.getBitmapWithBackground()

        // Proses gambar menjadi byte buffer
        val byteBuffer = ModelUtil.processImage(bitmap)

        // Ambil confidence dan prediksi label
        val (confidence, predictedLabel) = ModelUtil.getAccuracyForTargetIndexandLabel(
            tflite,
            byteBuffer,
            getTargetIndexForHuruf(randomQuestions[startQuestion])
        )

        // Tentukan apakah jawaban benar
        if (confidence >= 2) { // Confidence threshold bisa disesuaikan
            isCorrect = true
            correctAnswers++
        } else {
            incorrectAnswers++
        }

        // Soal saat ini
        val currentQuestion = randomQuestions[startQuestion]

        // Simpan hasil sementara
        val result = TempResultTest(
            bitmapDrawingView = bitmap,
            confidence = confidence,
            predictedLabel = predictedLabel,
            letter = currentQuestion,
            numberTest = startQuestion + 1, // Soal dimulai dari 1
            isCorrect = isCorrect
        )
        tempResultTests.add(result)
        showFeedbackDialog(isCorrect,confidence)
    }

    private fun showFeedbackDialog(isCorrect: Boolean, confidence: Int) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_test, null)
        val dialogScore = dialogView.findViewById<TextView>(R.id.tv_score)
        val dialogFeedback = dialogView.findViewById<TextView>(R.id.feedback_condition)
        val dialogButton = dialogView.findViewById<TextView>(R.id.btn_next_dialog)

        // Set nilai pada dialog
        dialogScore.text = "$confidence%"
        dialogFeedback.text = if (isCorrect) "Kerja Bagus !" else "Ayo Coba Lagi !"
        dialogView.setBackgroundResource(
            if (isCorrect) R.drawable.rounded_layout_test_green else R.drawable.rounded_layout_test_red
        )

        // Tampilkan dialog
        val customDialog = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Coroutine untuk menutup dialog otomatis setelah 3 detik
        lifecycleScope.launch {
            delay(3000) // Tunggu 3 detik
            if (customDialog.isShowing) { // Pastikan dialog masih terbuka
                moveToNextQuestion()
                customDialog.dismiss()
            }
        }

        dialogButton.setOnClickListener {
            moveToNextQuestion()
            customDialog.dismiss()
        }

        customDialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Batalkan timer saat fragment dihancurkan
    }

    private fun getTargetIndexForHuruf(huruf: String): Int {
        return when (huruf) {
            "Kho" -> 0
            "Tsa" -> 1
            "Dzal" -> 2
            "Kha" -> 3
            "Ta" -> 4
            "Jim" -> 5
            "Dal" -> 6
            "Ra" -> 7
            "Ba" -> 8
            "Tzo" -> 9
            "Ain" -> 10
            "Za" -> 11
            "Tho" -> 12
            "Ghain" -> 13
            "Fa" -> 14
            "Shod" -> 15
            "Sin" -> 16
            "Syin" -> 17
            "Dhod" -> 18
            "Qaf" -> 19
            "Ha" -> 20
            "Nun" -> 21
            "Lam" -> 22
            "Kaf" -> 23
            "Mim" -> 24
            "Wau" -> 25
            "Ya" -> 26
            "Hamzah" -> 27
            "Lam Alif" -> 28
            "Alif" -> 29
            else -> R.drawable.alifgif // Default GIF jika huruf tidak ditemukan
        }
    }

    private fun getSoundForHuruf(huruf: String): Int {
        return when (huruf) {
            "Alif" -> R.raw.sound1
            "Ba" -> R.raw.sound2
            "Ta" -> R.raw.sound3
            "Tsa" -> R.raw.sound4
            "Jim" -> R.raw.sound5
            "Kha" -> R.raw.sound6
            "Kho" -> R.raw.sound7
            "Dal" -> R.raw.sound8
            "Dzal" -> R.raw.sound9
            "Ra" -> R.raw.sound10
            "Za" -> R.raw.sound11
            "Sin" -> R.raw.sound12
            "Syin" -> R.raw.sound13
            "Shod" -> R.raw.sound14
            "Dhod" -> R.raw.sound15
            "Tho" -> R.raw.sound16
            "Tzo" -> R.raw.sound17
            "Ain" -> R.raw.sound18
            "Ghain" -> R.raw.sound19
            "Fa" -> R.raw.sound20
            "Qaf" -> R.raw.sound21
            "Kaf" -> R.raw.sound22
            "Lam" -> R.raw.sound23
            "Mim" -> R.raw.sound24
            "Nun" -> R.raw.sound25
            "Wau" -> R.raw.sound26
            "Ha" -> R.raw.sound27
            "Lam Alif" -> R.raw.sound28
            "Hamzah" -> R.raw.sound29
            "Ya" -> R.raw.sound30
            else -> R.raw.sound1 // Kembali ke default jika huruf tidak ditemukan
        }
    }


    companion object {
        val classLabelsModel = arrayOf(
            "Kho", "Tsa", "Dzal", "Kha", "Ta","Jim","Dal","Ra","Ba","Tzo","Ain",
            "Za","Tho","Ghain","Fa","Shod","Sin","Syin","Dhod","Qaf","Ha","Nun","Lam",
            "Kaf","Mim","Wau","Ya","Hamzah","Lam Alif","Alif"
        )
    }
}




