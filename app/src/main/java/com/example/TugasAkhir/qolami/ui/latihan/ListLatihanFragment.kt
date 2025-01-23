package com.example.TugasAkhir.qolami.ui.latihan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.LessonAdapter
import com.example.TugasAkhir.qolami.data.LessonItem
import com.example.TugasAkhir.qolami.databinding.FragmentListLatihanBinding
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.ui.latihan.gallery.LessonGalleryFragment
import com.example.TugasAkhir.qolami.ui.latihan.gallery.PracticeGalleryFragment
import com.example.TugasAkhir.qolami.ui.pelajaran.DetailLessonFragment
import com.example.TugasAkhir.qolami.ui.test.WritingTestHomeFragment

class ListLatihanFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var detailLessonFragment: DetailLessonFragment
    private lateinit var galleryFragment: LessonGalleryFragment
    private lateinit var binding: FragmentListLatihanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListLatihanBinding.inflate(inflater, container, false)

        recyclerView = binding.rvLesson
        galleryFragment=LessonGalleryFragment()

        binding.navIcTest.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WritingTestHomeFragment())
                .addToBackStack(null)
                .commit()
        }



        // Dummy data
        val lessons = listOf(
            LessonItem("Pelajaran 1", listOf("Alif"), listOf("sound1.mp3"), listOf(R.drawable.alif, 0, 0), listOf(R.drawable.alifgif)),
            LessonItem("Pelajaran 2", listOf("Kaf"), listOf("sound22.mp3"), listOf(R.drawable.kaf, 0, 0), listOf(R.drawable.kafgif)),
            LessonItem("Pelajaran 3", listOf("Lam"), listOf("sound23.mp3"), listOf(R.drawable.lam, 0, 0), listOf(R.drawable.lamgif)),
            LessonItem("Pelajaran 4", listOf("Lam Alif"), listOf("sound28.mp3"), listOf(R.drawable.lamalif, 0, 0), listOf(R.drawable.lamalifgif)),
            LessonItem("Pelajaran 5", listOf("Tsa", "Ta", "Ba"), listOf("sound4.mp3", "sound3.mp3", "sound2.mp3"), listOf(R.drawable.sa, R.drawable.ta, R.drawable.ba), listOf(R.drawable.tsagif, R.drawable.tagif, R.drawable.bagif)),
            LessonItem("Pelajaran 6", listOf("Nun"), listOf("sound25.mp3"), listOf(R.drawable.nun, 0, 0), listOf(R.drawable.nungif)),
            LessonItem("Pelajaran 7", listOf("Za", "Ra"), listOf("sound11.mp3", "sound10.mp3"), listOf(R.drawable.za, R.drawable.ra, 0), listOf(R.drawable.zaigif, R.drawable.ragif)),
            LessonItem("Pelajaran 8", listOf("Dzal", "Dal"), listOf("sound9.mp3", "sound8.mp3"), listOf(R.drawable.dzal, R.drawable.dal, 0), listOf(R.drawable.dzalgif, R.drawable.dalgif)),
            LessonItem("Pelajaran 9", listOf("Fa"), listOf("sound20.mp3"), listOf(R.drawable.fa, 0, 0), listOf(R.drawable.fagif)),
            LessonItem("Pelajaran 10", listOf("Qaf"), listOf("sound21.mp3"), listOf(R.drawable.kof, 0, 0), listOf(R.drawable.qofgif)),
            LessonItem("Pelajaran 11", listOf("Wau"), listOf("sound26.mp3"), listOf(R.drawable.waw, 0, 0), listOf(R.drawable.wawgif)),
            LessonItem("Pelajaran 12", listOf("Kho", "Kha", "Jim"), listOf("sound7.mp3", "sound6.mp3", "sound5.mp3"), listOf(R.drawable.kho, R.drawable.kha, R.drawable.jim), listOf(R.drawable.khogif, R.drawable.khogif, R.drawable.jimgif)),
            LessonItem("Pelajaran 13", listOf("Hamzah"), listOf("sound29.mp3"), listOf(R.drawable.hamzah, 0, 0), listOf(R.drawable.hamzahgif)),
            LessonItem("Pelajaran 14", listOf("Ain", "Ghain"), listOf("sound18.mp3", "sound19.mp3"), listOf(R.drawable.ain, R.drawable.goin, 0), listOf(R.drawable.aingif, R.drawable.ghoingif)),
            LessonItem("Pelajaran 15", listOf("Sin", "Syin"), listOf("sound12.mp3", "sound13.mp3"), listOf(R.drawable.sin, R.drawable.syin, 0), listOf(R.drawable.singif, R.drawable.syingif)),
            LessonItem("Pelajaran 16", listOf("Tzo", "Tho"), listOf("sound17.mp3", "sound16.mp3"), listOf(R.drawable.tzo, R.drawable.tho, 0), listOf(R.drawable.dhlogif, R.drawable.thogif)),
            LessonItem("Pelajaran 17", listOf("Shod", "Dhod"), listOf("sound14.mp3", "sound15.mp3"), listOf(R.drawable.shod, R.drawable.dzo, 0), listOf(R.drawable.shodgif, R.drawable.dhadgif)),
            LessonItem("Pelajaran 18", listOf("Mim"), listOf("sound24.mp3"), listOf(R.drawable.mim, 0, 0), listOf(R.drawable.mimgif)),
            LessonItem("Pelajaran 19", listOf("HA"), listOf("sound27.mp3"), listOf(R.drawable.ha, 0, 0), listOf(R.drawable.hagif)),
            LessonItem("Pelajaran 20", listOf("Ya"), listOf("sound30.mp3"), listOf(R.drawable.ya, 0, 0), listOf(R.drawable.yagif)),
            LessonItem("Your Hijaiyah Gallery", listOf(""), listOf("", "", ""), listOf(0, 0, 0), listOf(0))
        )

        lessonAdapter = LessonAdapter(lessons) { lesson ->
            // Cek apakah ini adalah lesson terakhir
            val isLastLesson = lessons.last() == lesson

            // Jika lesson terakhir, arahkan ke LessonGalleryFragment
            if (isLastLesson) {
                val lessonGalleryFragment = LessonGalleryFragment()
                val practiceGalleryFragment= PracticeGalleryFragment()

                // Kirim data ke fragment jika diperlukan
                val bundle = Bundle()


                bundle.putString("lessonName", lesson.lessonName)
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
                bundle.putStringArrayList("lessonTexts", ArrayList(lesson.letter))
                bundle.putStringArrayList("lessonAudio", ArrayList(lesson.audioString))
                bundle.putString("lessonName", lesson.lessonName)
                bundle.putIntegerArrayList("lessonimg",ArrayList(lesson.images))
                bundle.putIntegerArrayList("lessonGif",ArrayList(lesson.gif))
                detailLessonFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detailLessonFragment)
                    .addToBackStack(null) // Tambahkan ke back stack
                    .commit()
            }
        }


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = lessonAdapter

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle tombol back dari perangkat
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}
