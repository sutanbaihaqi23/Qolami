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
import com.example.TugasAkhir.qolami.data.QuestionItem
import com.example.TugasAkhir.qolami.data.TempResultTest
import com.example.TugasAkhir.qolami.data.TempResultTest2
import com.example.TugasAkhir.qolami.databinding.FragmentTest2Binding
import com.example.TugasAkhir.qolami.util.ModelUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter


class Test2Fragment : Fragment() {
    private lateinit var binding: FragmentTest2Binding
    private var startQuestion = 5 // Mulai dari 0 agar soal pertama dimulai dari random
    private val totalQuestions = 10
    private lateinit var questionAdapter: QuestionAdapter
    private var timer: CountDownTimer? = null
    private var timePerQuestion: Long = 11000
    private lateinit var tflite: Interpreter
    private lateinit var drawingView: DrawingView
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var testResults = mutableListOf<TempResultTest>()
    private var testResults2 = mutableListOf<TempResultTest2>()
    private var remainingTime: Long = timePerQuestion
    private var mediaPlayer: MediaPlayer? = null


    private val allQuestions = listOf(
        QuestionItem(R.drawable.ba, "Ba", R.drawable.ic_ask, "Ta", R.drawable.sa, "Tsa", "Ta"),
        QuestionItem(R.drawable.jim, "Jim", R.drawable.ic_ask, "Kha", R.drawable.kho, "Kho", "Kha"),
        QuestionItem(R.drawable.dal, "Dal", R.drawable.ic_ask, "Dza", R.drawable.ra, "Ra", "Dza"),
        QuestionItem(R.drawable.za, "Za", R.drawable.ic_ask, "Sin", R.drawable.syin, "Syin", "Sin"),
        QuestionItem(R.drawable.shod, "Shod", R.drawable.ic_ask, "Tho", R.drawable.tho, "Dhad", "Tho"),
        QuestionItem(R.drawable.ain, "Ain", R.drawable.ic_ask, "Fa", R.drawable.fa, "Ghain", "Fa"),
        QuestionItem(R.drawable.kof, "Qof", R.drawable.ic_ask, "Kaf", R.drawable.lam, "Kaf", "Kaf"),
        QuestionItem(R.drawable.mim, "Mim", R.drawable.ic_ask, "Nun", R.drawable.waw, "Nun", "Nun")
    ).shuffled()


    private  val question=allQuestions.take(5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTest2Binding.inflate(inflater, container, false)
        tflite = ModelUtil.loadModel(requireContext(), "model_hijaiyah_baru_dataset_ditambah.tflite")
        drawingView = binding.drawingView
        arguments?.let {
            correctAnswers = it.getInt("correctAnswers", 0)
            incorrectAnswers = it.getInt("incorrectAnswers", 0)
            testResults = it.getParcelableArrayList<TempResultTest>("tempResultTests") ?: arrayListOf()
        }
        Log.d("ResultTestFragment", "results: $testResults")

        questionAdapter = QuestionAdapter(totalQuestions, startQuestion)
        binding.rvNumberQuestion.layoutManager = GridLayoutManager(
            requireContext(),
            5, // Jumlah item per baris
            GridLayoutManager.VERTICAL,
            false
        )
        binding.rvNumberQuestion.adapter = questionAdapter
        binding.submitButton.setOnClickListener {
            checkAnswer()
            timer?.cancel()// Cek jawaban saat tombol submit ditekan
        }

        binding.icSpeaker.setOnClickListener {
            val soundResId = getSoundForHuruf(question[startQuestion - 5].answer)
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
            mediaPlayer?.start()

        }
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        binding.icEraser.setOnClickListener {
            drawingView.clearCanvas()
        }

        binding.btnBackToHomeTest.setOnClickListener {
            showQuitExamDialog()
        }
        startTimer()
        displayQuestion()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle tombol back dari perangkat
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            timer?.cancel()
            showQuitExamDialog()
        }
    }

    private fun showQuitExamDialog() {
        correctAnswers = 0
        incorrectAnswers = 0
        startQuestion=0
        testResults2.clear()
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Quit Exam?")
            .setMessage("Are you sure you want to Quit the exam?")
            .setPositiveButton("Yes") { dialog, _ ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WritingTestHomeFragment())
                    .addToBackStack(null)
                    .commit()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                startTimer() // Jika Anda memiliki timer dan ingin melanjutkannya.
            }
            .create()

        // Membuat dialog tidak dapat dibatalkan dengan cara lain selain menekan tombol Yes/No
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.show()
    }

    private fun checkAnswer() {
        val bitmap = drawingView.getBitmapWithBackground()
        val byteBuffer = ModelUtil.processImage(bitmap)
        val (predictedLabel, confidence) = ModelUtil.runInferenceWithConfidence(tflite, byteBuffer)
        val currentQuestion = question[startQuestion - 5]
        val (confidence2, predictedLabel2)=ModelUtil.getAccuracyForTargetIndexandLabel(tflite, byteBuffer,getTargetIndexForHuruf(currentQuestion.answer))// Ambil soal sesuai dengan startQuestion
        val correctAnswer = currentQuestion.answer
        val isCorrect = predictedLabel2 == correctAnswer

        if (predictedLabel2 == correctAnswer && confidence2 >= 2 ) {
            correctAnswers++
        } else {
            incorrectAnswers++
        }


        val tempResult = TempResultTest2(
            bitmapDrawingView = bitmap,
            confidence = confidence2,
            predictedLabel = predictedLabel2,
            leftLetter = currentQuestion.lastLetterText, // Ganti dengan data relevan jika ada
            middleLetter = currentQuestion.answer, // Contoh: Jawaban tengah
            rightLetter = currentQuestion.firstLetterText, // Ganti dengan data relevan jika ada
            leftLetterImg = currentQuestion.firstLetter,
            middleLetterImg = currentQuestion.middleLetter,
            rightLetterImg = currentQuestion.lastLetter,
            numberTest = startQuestion +1,
            isCorrect = isCorrect
        )
        testResults2.add(tempResult)

        showFeedbackDialog(isCorrect,confidence2)
    }
    private fun showFeedbackDialog(isCorrect: Boolean, confidence: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_test, null)
        val dialogScore = dialogView.findViewById<TextView>(R.id.tv_score)
        val dialogFeedback = dialogView.findViewById<TextView>(R.id.feedback_condition)
        val dialogButton = dialogView.findViewById<TextView>(R.id.btn_next_dialog)

        // Set nilai pada dialog
        dialogScore.text = "$confidence%"
        dialogFeedback.text = if (isCorrect) "Good Job!" else "Try Again"
        dialogView.setBackgroundResource(
            if (isCorrect) R.drawable.rounded_layout_test_green else R.drawable.rounded_layout_test_red
        )

        // Tampilkan dialog
        val customDialog = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(false)
            .create()

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


        private fun displayQuestion() {
            if (startQuestion < totalQuestions) {
                // Ambil soal sesuai startQuestion dari allQuestions
                val currentQuestion = question[startQuestion - 5] // Offset startQuestion ke 0
                binding.imgLeftLetterQuestion.setImageResource(currentQuestion.firstLetter)
                binding.imgMiddleLetterQuestion.setImageResource(currentQuestion.middleLetter)
                binding.imgRightLetterQuestion.setImageResource(currentQuestion.lastLetter)
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
                checkAnswer()// Reset waktu tersisa untuk soal berikutnya
                
            }
        }.start()
    }

    private fun resetTimer() {
        remainingTime = timePerQuestion // Reset waktu ke durasi penuh
    }

    private fun moveToNextQuestion() {
        resetTimer()

        startQuestion++
        if (startQuestion < totalQuestions) {
            questionAdapter.updateCurrentQuestion(startQuestion)
            drawingView.clearCanvas()
            displayQuestion()
            startTimer()
        } else {
            // Tes selesai
            timer?.cancel()
            sendTestCompletionTest()
            onDestroyView()
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

    private fun sendTestCompletionTest() {
        // Buat bundle untuk mengirim data hasil tes
        val bundle = Bundle().apply {
            putInt("correctAnswers", correctAnswers)
            putInt("incorrectAnswers", incorrectAnswers)
            putParcelableArrayList("results", ArrayList(testResults))
            putParcelableArrayList("results2", ArrayList(testResults2))
        }

        // Arahkan ke ResultTestFragment dengan data yang dikirimkan melalui bundle
        val resultTestFragment = ResultTestFragment().apply {
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultTestFragment)
            .addToBackStack(null) // Tambahkan ke backstack jika perlu kembali ke fragment sebelumnya
            .commit()
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

}