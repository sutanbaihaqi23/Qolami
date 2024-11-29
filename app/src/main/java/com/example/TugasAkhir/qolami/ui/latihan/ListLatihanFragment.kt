package com.example.TugasAkhir.qolami.ui.latihan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.LessonAdapter
import com.example.TugasAkhir.qolami.adapter.PelajaranAdapter
import com.example.TugasAkhir.qolami.data.LessonItem
import com.example.TugasAkhir.qolami.ui.latihan.gallery.LessonGalleryFragment
import com.example.TugasAkhir.qolami.ui.pelajaran.DetailLessonFragment

class ListLatihanFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var detailLessonFragment: DetailLessonFragment
    private lateinit var galleryFragment: LessonGalleryFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_latihan, container, false)
        recyclerView = view.findViewById(R.id.rv_lesson)
        galleryFragment=LessonGalleryFragment()

        // Dummy data
        val lessons = listOf(
            LessonItem("Lesson 1", listOf(R.drawable.alif,0,0)),
            LessonItem("Lesson 2", listOf(R.drawable.ba, R.drawable.ba, R.drawable.ba)),
            LessonItem("Lesson 3", listOf(R.drawable.ta, R.drawable.ta, R.drawable.ta))
        )

        // Navigasi saat item diklik
        /*lessonAdapter = LessonAdapter(lessons) { lesson ->
            // Replace fragment dengan FragmentTransaction
            detailLessonFragment = DetailLessonFragment()

            // Kirim data melalui arguments
            val bundle = Bundle()
            bundle.putString("lessonName", lesson.name)
            detailLessonFragment.arguments = bundle

            // Ganti fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailLessonFragment)
                .addToBackStack(null) // Tambahkan ke back stack
                .commit()
        }*/

        lessonAdapter = LessonAdapter(lessons) { lesson ->
            // Cek apakah ini adalah lesson terakhir
            val isLastLesson = lessons.last() == lesson

            // Jika lesson terakhir, arahkan ke LessonGalleryFragment
            if (isLastLesson) {
                val lessonGalleryFragment = LessonGalleryFragment()

                // Kirim data ke fragment jika diperlukan
                val bundle = Bundle()
                bundle.putString("lessonName", lesson.name)
                lessonGalleryFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, lessonGalleryFragment)
                    .addToBackStack(null) // Tambahkan ke back stack
                    .commit()
            } else {
                // Jika bukan lesson terakhir, arahkan ke DetailLessonFragment
                detailLessonFragment = DetailLessonFragment()

                // Kirim data ke fragment jika diperlukan
                val bundle = Bundle()
                bundle.putString("lessonName", lesson.name)
                detailLessonFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detailLessonFragment)
                    .addToBackStack(null) // Tambahkan ke back stack
                    .commit()
            }
        }


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = lessonAdapter

        return view
    }
}
