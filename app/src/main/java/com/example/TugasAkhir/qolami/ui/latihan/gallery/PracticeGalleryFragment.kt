package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.GalleryPracticeAdapter
import com.example.TugasAkhir.qolami.data.GalleryItem
import com.example.TugasAkhir.qolami.databinding.FragmentPracticeGalleryBinding


class PracticeGalleryFragment : Fragment() {
    private lateinit var binding: FragmentPracticeGalleryBinding
    private lateinit var detailGalleryPracticeFragment: DetailGalleryPracticeFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPracticeGalleryBinding.inflate(inflater, container, false)
        val rvGalleryPractice = binding.rvGalleryPractice
        val galleryItems = listOf(
            GalleryItem(R.drawable.alif),
            GalleryItem(R.drawable.ba),
            GalleryItem(R.drawable.ta),
            GalleryItem(R.drawable.ba),
            GalleryItem(R.drawable.ta)
        )

        val adapter = GalleryPracticeAdapter(galleryItems) { galleryItem ->
            detailGalleryPracticeFragment=DetailGalleryPracticeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailGalleryPracticeFragment)
                .addToBackStack(null)
                .commit()
        }
        rvGalleryPractice.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        rvGalleryPractice.adapter = adapter

        return binding.root
    }

}