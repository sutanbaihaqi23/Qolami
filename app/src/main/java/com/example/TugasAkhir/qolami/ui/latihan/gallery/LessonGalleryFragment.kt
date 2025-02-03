package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.GalleryLessonAdapter
import com.example.TugasAkhir.qolami.databinding.FragmentLessonGalleryBinding
import com.example.TugasAkhir.qolami.ui.latihan.ListLatihanFragment


class LessonGalleryFragment : Fragment() {

    private lateinit var binding : FragmentLessonGalleryBinding
    private lateinit var galleryLessonAdapter: GalleryLessonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonGalleryBinding.inflate(inflater, container, false)
        binding.btnBackToList.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListLatihanFragment())
                .addToBackStack(null)
                .commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListLatihanFragment())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        galleryLessonAdapter = GalleryLessonAdapter()
        binding.rvGalleryLesson.apply {
            layoutManager = GridLayoutManager(context, 5).apply {
                reverseLayout = false
                spanCount = 5
            }
            adapter = galleryLessonAdapter
            setHasFixedSize(true)
        }
    }


    private fun setupClickListeners() {
        binding.btnPracticeGallery.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PracticeGalleryFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}