package com.example.TugasAkhir.qolami.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.TugasAkhir.qolami.R

class DetailPelajaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_pelajaran, container, false)

        // Ambil data huruf dan gambar dari arguments
        val hurufDetail = arguments?.getString("HURUF_DETAIL")
        val hurufImageRes = arguments?.getInt("HURUF_IMAGE")

        // Tampilkan data di ImageView dan GIF
        val imageViewHuruf: ImageView = view.findViewById(R.id.imageViewHuruf)
       // val gifViewHuruf: ImageView = view.findViewById(R.id.gifViewHuruf)

        // Set gambar huruf
        imageViewHuruf.setImageResource(hurufImageRes ?: R.drawable.alif)

        // Set GIF huruf (pastikan ada resource GIF untuk setiap huruf)
        /*val gifResId = when (hurufDetail) {
            "Alif" -> R.drawable.alif_gif
            "Ba" -> R.drawable.ba_gif
            // Tambahkan case lainnya
            else -> R.drawable.default_gif
        }
        gifViewHuruf.setImageResource(gifResId)*/

        return view
    }
}
