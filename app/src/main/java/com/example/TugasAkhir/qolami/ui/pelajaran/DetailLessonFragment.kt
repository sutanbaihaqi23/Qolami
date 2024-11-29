package com.example.TugasAkhir.qolami.ui.pelajaran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.customview.DrawingView
import com.example.TugasAkhir.qolami.databinding.FragmentDetailLessonBinding

class DetailLessonFragment : Fragment() {
    private lateinit var drawingView: DrawingView
    private lateinit var binding: FragmentDetailLessonBinding

    private var isPhaseOne = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailLessonBinding.inflate(inflater, container, false)
        drawingView = binding.drawingView
        // Ambil data huruf dan gambar dari arguments
        val hurufDetail = arguments?.getString("HURUF_DETAIL")
        val hurufImageRes = arguments?.getInt("HURUF_IMAGE")

        //binding.imageViewHuruf.setImageResource(hurufImageRes ?: R.drawable.alif)
        binding.imageViewHuruf.setImageResource(R.drawable.alif)

        binding.tvAccuracy.visibility = View.GONE
        binding.btnClearLesson.visibility=View.GONE
        binding.drawingView.visibility=View.GONE

        binding.btnPractice.setOnClickListener {
            if (isPhaseOne){
                enterPhaseTwo()
            }
        }

        // Tampilkan data di ImageView dan GIF

       // val gifViewHuruf: ImageView = view.findViewById(R.id.gifViewHuruf)

        // Set gambar huruf


        // Set GIF huruf (pastikan ada resource GIF untuk setiap huruf)
        /*val gifResId = when (hurufDetail) {
            "Alif" -> R.drawable.alif_gif
            "Ba" -> R.drawable.ba_gif
            // Tambahkan case lainnya
            else -> R.drawable.default_gif
        }
        gifViewHuruf.setImageResource(gifResId)*/

        binding.btnClearLesson.setOnClickListener {
            drawingView.clearCanvas()
        }

        return binding.root
    }

    fun enterPhaseTwo() {
        isPhaseOne = false
        binding.btnPractice.text = "Check"
        binding.tvAccuracy.visibility = View.VISIBLE
        binding.btnClearLesson.visibility=View.VISIBLE
        binding.drawingView.visibility=View.VISIBLE
    }
}
