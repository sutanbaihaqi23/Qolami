package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentDetailGalleryPracticeBinding


class DetailGalleryPracticeFragment : Fragment() {

    private lateinit var binding: FragmentDetailGalleryPracticeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDetailGalleryPracticeBinding.inflate(inflater, container, false)
        return binding.root
    }
}