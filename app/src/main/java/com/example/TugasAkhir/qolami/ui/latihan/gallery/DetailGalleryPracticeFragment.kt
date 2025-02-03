package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.customview.DrawingView
import com.example.TugasAkhir.qolami.data.Drawing
import com.example.TugasAkhir.qolami.database.AppDatabase
import com.example.TugasAkhir.qolami.databinding.FragmentDetailGalleryPracticeBinding
import com.example.TugasAkhir.qolami.util.ModelUtil
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.File
import com.google.android.material.snackbar.Snackbar


class DetailGalleryPracticeFragment : Fragment() {

    private lateinit var letterName: String
    private lateinit var authViewModel: AuthViewModel
    private lateinit var tflite: Interpreter
    private var isTrue=false
    private var currentBitmap: Bitmap? = null
    private var label=""
    private var confidence=0


    private lateinit var binding: FragmentDetailGalleryPracticeBinding

    private var mediaPlayer: MediaPlayer? = null
    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding= FragmentDetailGalleryPracticeBinding.inflate(inflater, container, false)
        letterName= arguments?.getString("letter_name") ?: ""
        binding.titleHurufDetail.text=letterName
        val userid=authViewModel.getUserUid()

        tflite= ModelUtil.loadModel(requireContext(),"model_hijaiyah_baru_dataset_ditambah.tflite")

        binding.btnSaveDetailGallery.isVisible=false




        binding.btnEraserDetailGallery.setOnClickListener {
            binding.drawingView.clearCanvas()
            binding.tvAccuracyDetailGallery.text = "Skor : "
        }

        binding.btnSpeakerDetailGallery.setOnClickListener {
            playSoundForLetter(letterName)
        }

        binding.icBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PracticeGalleryFragment())
                .addToBackStack(null)
                .commit()
        }

        //buat handle bawaan back device
        requireActivity().onBackPressedDispatcher.addCallback {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PracticeGalleryFragment())
                .addToBackStack(null)
                .commit()
        }


        binding.btnCheckDetailGallery.setOnClickListener {
            binding.drawingView.post {
                val bitmap = binding.drawingView.getBitmapWithBackground()
                if (bitmap == null) {
                    Log.e("Error", "Bitmap tidak dapat dibuat. Ukuran view tidak valid.")
                    Toast.makeText(requireContext(), "Gambar tidak valid, coba lagi.", Toast.LENGTH_SHORT).show()
                    return@post
                }

                // Simpan bitmap untuk digunakan nanti
                currentBitmap = bitmap

                val bytebuffer = ModelUtil.processImage(bitmap)
                val (confidence, label) = ModelUtil.getAccuracyForTargetIndexandLabel(
                    tflite, bytebuffer, getTargetIndexForHuruf(letterName)
                )
                this.confidence = confidence
                this.label = label

                // Periksa apakah huruf yang digambar cocok
                if (label == letterName && confidence >= 30) {
                    isTrue = true
                    binding.btnSaveDetailGallery.isVisible = true
                    binding.tvAccuracyDetailGallery.text = "Skor : $confidence%"
                } else {
                    isTrue = false
                    binding.btnSaveDetailGallery.isVisible = false
                    binding.tvAccuracyDetailGallery.text = "Skor : "
                }

                // Tampilkan Snackbar dengan pesan sesuai hasil pengecekan
                showSnackbar(if (isTrue) "Huruf yang di gambar cocok!" else "Huruf yang di gambar tidak cocok!")
            }
        }


        binding.btnSaveDetailGallery.setOnClickListener {
            val drawingView = binding.drawingView
            var bitmap = drawingView.getBitmapWithBackground()
            if (bitmap != null) {
                saveBitmapToDatabase(bitmap, userid.toString(), letterName)
            } else {
                Toast.makeText(requireContext(), "Gambar tidak valid", Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val snackbarView = layoutInflater.inflate(R.layout.snackbar_galeri, null)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        // Hapus semua child view sebelumnya (jika ada)
        snackbarLayout.removeAllViews()

        // Tambahkan custom view ke Snackbar
        snackbarLayout.addView(snackbarView, 0)
        snackbarLayout.setPadding(0)
        // Set pesan ke TextView di custom layout
        val messageTextView = snackbarView.findViewById<TextView>(R.id.snackbar_message)
        messageTextView.text = message

        // Tampilkan Snackbar
        snackbar.show()
    }

    private fun playSoundForLetter(letter: String) {
        // Map huruf ke nomor suara
        val letterNames = listOf(
            "Alif", "Ba", "Ta", "Tsa", "Jim", "Kha", "Kho", "Dal", "Dzal", "Ra", "Za",
            "Sin", "Syin", "Shod", "Dhod", "Tho", "Tzo", "Ain", "Ghain", "Fa", "Qaf",
            "Kaf", "Lam", "Mim", "Nun", "Wau", "Ha", "Lam Alif", "Hamzah", "Ya"
        )

        val index = letterNames.indexOf(letter) + 1 // Urutan dimulai dari 1
        if (index in 1..letterNames.size) {
            val soundResId = resources.getIdentifier("sound$index", "raw", requireContext().packageName)
            if (soundResId != 0) {
                mediaPlayer?.release() // Hentikan suara sebelumnya jika ada
                mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
                mediaPlayer?.start()
            } else {
                Toast.makeText(requireContext(), "File suara tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Huruf tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Bebaskan sumber daya MediaPlayer
    }



    private fun saveBitmapToDatabase(bitmap: Bitmap, user: String, letterName: String) {
        // Ganti penamaan file dengan letterName
        val filename = "drawing_${letterName}.png"  // Menggunakan letterName untuk nama file
        val imagesDir = requireContext().filesDir.absolutePath + "/drawings"  // Gunakan requireContext() jika di dalam Fragment
        val file = File(imagesDir, filename)

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()  // Pastikan direktori ada
        }

        val outputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Simpan path ke database
        val drawingDao = AppDatabase.getDatabase(requireContext()).drawingDao() // Gunakan requireContext() untuk context di Fragment
        CoroutineScope(Dispatchers.IO).launch {
            drawingDao.insert(Drawing(userId = user, letterName= letterName, imagePath = file.absolutePath))  // Simpan ke database
        }

        // Menampilkan Toast menggunakan requireContext()
        Toast.makeText(requireContext(), "Gambar berhasil disimpan ke database", Toast.LENGTH_SHORT).show()
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