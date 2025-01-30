package com.example.TugasAkhir.qolami.ui.pelajaran

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.customview.DrawingView
import com.example.TugasAkhir.qolami.databinding.FragmentDetailLessonBinding
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.util.ModelUtil

import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileOutputStream

class DetailLessonFragment : Fragment() {
    private lateinit var drawingView: DrawingView
    private lateinit var binding: FragmentDetailLessonBinding
    private lateinit var tflite: Interpreter
    private lateinit var utilModel: ModelUtil
    private var currentBitmap: Bitmap? = null
    private var index=0
    private var isPhaseOne = true
    private var mediaPlayer: MediaPlayer? = null
    private var gifResId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailLessonBinding.inflate(inflater, container, false)
        drawingView = binding.drawingView
        binding.tvAccuracy.text="Skor : "



        tflite = ModelUtil.loadModel(requireContext(), "model_hijaiyah_baru_dataset_ditambah.tflite")




        // Ambil data huruf dan gambar dari arguments
        val huruf = arguments?.getStringArrayList("lessonTexts")
        val hurufImageRes = arguments?.getIntegerArrayList("lessonimg")
        val audio=arguments?.getStringArrayList("lessonAudio")


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isPhaseOne){
                requireActivity().supportFragmentManager.popBackStack()
            }else {
                updateUI(huruf!!, hurufImageRes!!, audio!!)
            }
        }

        binding.icArrowBackPelajaran.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        if (huruf.isNullOrEmpty() || hurufImageRes.isNullOrEmpty() || audio.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Data tidak tersedia!", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        when (huruf.size) {
            1 -> setupSingleLesson(huruf, hurufImageRes, audio)
            2 -> setupDoubleLesson(huruf, hurufImageRes, audio)
            3 -> setupTripleLesson(huruf, hurufImageRes, audio)
            else -> Toast.makeText(requireContext(), "Jumlah huruf tidak didukung!", Toast.LENGTH_SHORT).show()
        }

        binding.tvAccuracy.visibility = View.GONE
        binding.btnClearLesson.visibility=View.GONE
        binding.drawingView.visibility=View.GONE


        binding.btnPractice.setOnClickListener {
            if (isPhaseOne){
                enterPhaseTwo()
            }else{
                val bitmap = drawingView.getBitmapWithBackground()
                bitmap?.let { bitmap ->
                    currentBitmap = bitmap

                    val bytebuffer=ModelUtil.processImage(currentBitmap!!)
                    val result=ModelUtil.runInference(tflite,bytebuffer)


                    val getIndex = ModelUtil.getAccuracyForTargetIndex(tflite,bytebuffer,getTargetIndexForHuruf(huruf[index]))
                    Log.d("Result Log",result)


                    binding.tvAccuracy.text="Skor : "+getIndex.toString()+"%"


                } ?: run {
                    Toast.makeText(requireContext(), "No drawing detected", Toast.LENGTH_SHORT).show()
                }



            }
        }


        binding.btnClearLesson.setOnClickListener {
            drawingView.clearCanvas()
            binding.tvAccuracy.text="Skor : "
        }

        return binding.root
    }

    private fun setupSingleLesson(huruf: List<String>, hurufImageRes: List<Int>, lessonAudioString: List<String>) {
        binding.icBackHuruf.isVisible = false
        binding.icNextHuruf.isVisible = false
        binding.tvLatinHuruf.text = huruf[0]
        binding.imageViewHuruf.setImageResource(hurufImageRes[0])

        binding.icSpeaker.setOnClickListener {
            playAudio(lessonAudioString[index])
        }

        // Atur GIF
        setGifWithGlide(huruf[0])

        // Atur audio
        mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(lessonAudioString[0]))
    }



    private fun setGifWithGlide(huruf: String) {
        gifResId = getGifForHuruf(huruf)
        Glide.with(this)
            .asGif()
            .load(gifResId)
            .into(binding.gifPelajaran)
    }
    private fun getGifForHuruf(huruf: String): Int {
        return when (huruf) {
            "Alif" -> R.drawable.alifgif
            "Ba" -> R.drawable.bagif
            "Ta" -> R.drawable.tagif
            "Kaf" -> R.drawable.kafgif
            "Lam" -> R.drawable.lamgif
            "Lam Alif" -> R.drawable.lamalifgif
            "Tsa" -> R.drawable.tsagif
            "Nun" -> R.drawable.nungif
            "Za" -> R.drawable.zaigif
            "Ra" -> R.drawable.ragif
            "Dzal" -> R.drawable.dzalgif
            "Dal" -> R.drawable.dalgif
            "Fa" -> R.drawable.fagif
            "Qaf" -> R.drawable.qofgif
            "Wau" -> R.drawable.waugif
            "Kho" -> R.drawable.khogif
            "Kha" -> R.drawable.khagif
            "Jim" -> R.drawable.jimgif
            "Hamzah" -> R.drawable.hamzahgif
            "Ain" -> R.drawable.aingif
            "Ghain" -> R.drawable.ghaingif
            "Sin" -> R.drawable.singif
            "Syin" -> R.drawable.syingif
            "Tzo" -> R.drawable.dhlogif
            "Tho" -> R.drawable.thogif
            "Shod" -> R.drawable.shodgif
            "Dhod" -> R.drawable.dhadgif
            "Mim" -> R.drawable.mimgif
            "Ha" -> R.drawable.hagif
            "Ya" -> R.drawable.yagif
            else -> R.drawable.alifgif// Default GIF jika huruf tidak ditemukan
        }
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



    private fun setupDoubleLesson(huruf: List<String>, hurufImageRes: List<Int>, lessonAudioString: List<String>) {
        updateUI(huruf, hurufImageRes, lessonAudioString)
        binding.icBackHuruf.setOnClickListener {
            if (index > 0) {
                index--
                updateUI(huruf, hurufImageRes, lessonAudioString)
                binding.drawingView.clearCanvas()

            }
        }
        binding.icNextHuruf.setOnClickListener {
            if (index < 1) {
                index++
                updateUI(huruf, hurufImageRes, lessonAudioString)
                binding.drawingView.clearCanvas()
            }
        }
    }

    private fun setupTripleLesson(huruf: List<String>, hurufImageRes: List<Int>, lessonAudioString: List<String>) {
        updateUI(huruf, hurufImageRes, lessonAudioString)
        binding.icBackHuruf.setOnClickListener {
            if (index > 0) {
                index--
                updateUI(huruf, hurufImageRes, lessonAudioString)
                binding.drawingView.clearCanvas()
            }
        }
        binding.icNextHuruf.setOnClickListener {
            if (index < 2) {
                index++
                updateUI(huruf, hurufImageRes, lessonAudioString)
                binding.drawingView.clearCanvas()
            }
        }
    }

    private fun updateUI(huruf: List<String>, hurufImageRes: List<Int>, lessonAudioString: List<String>) {
        // Reset ke Phase One
        isPhaseOne = true
        binding.drawingView.visibility = View.GONE
        binding.btnClearLesson.visibility = View.GONE
        binding.tvAccuracy.visibility = View.GONE
        binding.gifPelajaran.visibility = View.VISIBLE


        binding.tvAccuracy.text = "Skor : "

        // Perbarui tampilan untuk huruf baru
        binding.tvLatinHuruf.text = huruf[index]
        binding.imageViewHuruf.setImageResource(hurufImageRes[index])

        // Perbarui GIF
        setGifWithGlide(huruf[index])

        // Tampilkan tombol navigasi sesuai indeks
        binding.icBackHuruf.isVisible = index > 0
        binding.icNextHuruf.isVisible = index < huruf.size - 1

        // Atur ulang klik speaker
        binding.icSpeaker.setOnClickListener {
            playAudio(lessonAudioString[index])
        }
    }


    private fun playAudio(audioFileName: String) {
        try {
            // Hentikan audio sebelumnya jika sedang berjalan
            mediaPlayer?.stop()
            mediaPlayer?.release()

            // Dapatkan resource ID dari nama file di folder raw
            val resId = requireContext().resources.getIdentifier(
                audioFileName.substringBefore(".mp3"),
                "raw",
                requireContext().packageName
            )

            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(requireContext(), resId)
                mediaPlayer?.start()
            } else {
                Toast.makeText(requireContext(), "Audio tidak ditemukan: $audioFileName", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal memutar audio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun enterPhaseTwo() {
        // Hilangkan GIF
        binding.gifPelajaran.visibility = View.GONE

        // Tampilkan DrawingView dan tombol lainnya
        binding.drawingView.visibility = View.VISIBLE
        binding.btnClearLesson.visibility = View.VISIBLE
        binding.tvAccuracy.visibility = View.VISIBLE




        isPhaseOne = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
