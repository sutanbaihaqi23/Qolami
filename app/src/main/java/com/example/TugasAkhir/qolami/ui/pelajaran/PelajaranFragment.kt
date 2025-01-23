package com.example.TugasAkhir.qolami.ui.pelajaran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.PelajaranAdapter
import com.example.TugasAkhir.qolami.data.HurufModel

class PelajaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pelajaran, container, false)

        val hurufList = listOf(
            HurufModel("Alif", R.drawable.alif),
            HurufModel("Kaf", R.drawable.kaf),
            HurufModel("Lam", R.drawable.lam),
            // Tambahkan huruf lainnya
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_pelajaran)
        recyclerView.layoutManager = GridLayoutManager(context, 5)
        recyclerView.adapter = PelajaranAdapter(hurufList) { huruf ->
            // Buat Bundle untuk mengirim data ke DetailHurufFragment
            val bundle = Bundle().apply {
                putString("HURUF_DETAIL", huruf.huruf)
                putInt("HURUF_IMAGE", huruf.imageResource)
            }
            val detailFragment = DetailLessonFragment().apply {
                arguments = bundle
            }

            // Navigasi ke DetailHurufFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}

