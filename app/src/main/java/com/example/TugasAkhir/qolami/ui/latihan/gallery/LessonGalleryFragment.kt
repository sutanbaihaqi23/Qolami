package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentLessonGalleryBinding


class LessonGalleryFragment : Fragment() {
    private lateinit var binding: FragmentLessonGalleryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLessonGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPracticeGallery.setOnClickListener {
            val practiceGalleryFragment = PracticeGalleryFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, practiceGalleryFragment)
                .addToBackStack(null)
                .commit()
        }
    }


}