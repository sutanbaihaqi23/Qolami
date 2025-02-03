package com.example.TugasAkhir.qolami.ui.latihan.gallery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.GalleryPracticeAdapter
import com.example.TugasAkhir.qolami.data.Drawing
import com.example.TugasAkhir.qolami.data.GalleryItem
import com.example.TugasAkhir.qolami.database.AppDatabase
import com.example.TugasAkhir.qolami.databinding.FragmentPracticeGalleryBinding
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PracticeGalleryFragment : Fragment() {
    private lateinit var binding: FragmentPracticeGalleryBinding
    private lateinit var detailGalleryPracticeFragment: DetailGalleryPracticeFragment
    private lateinit var galleryItems: List<GalleryItem>
    private lateinit var viewModel: AuthViewModel

    // Pastikan Anda sudah mendapatkan userId (misalnya melalui SharedPreferences atau argumen fragment)
    private var userId: String = "user_id_here" // Gantilah dengan cara yang sesuai untuk mendapatkan userId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentPracticeGalleryBinding.inflate(inflater, container, false)

        userId= viewModel.getUserUid().toString()
        val rvGalleryPractice = binding.rvGalleryPractice


        // Ambil gambar yang sesuai dari database
        CoroutineScope(Dispatchers.Main).launch {
            val drawingDao = AppDatabase.getDatabase(requireContext()).drawingDao()
            val drawings = drawingDao.getDrawingsForUser(userId) // Mengambil gambar berdasarkan userId

            // Log untuk memeriksa data yang diambil dari database
            Log.d("PracticeGalleryFragment", "Drawings size: ${drawings.size}")
            drawings.forEach { drawing ->
                Log.d("PracticeGalleryFragment", "Drawing: ${drawing.letterName}, Path: ${drawing.imagePath}")
            }

            // Buat list galleryItems berdasarkan data dari database
            galleryItems = createGalleryItems(drawings)

            val adapter = GalleryPracticeAdapter(galleryItems) { galleryItem ->
                // Pass the letter name to the DetailGalleryPracticeFragment
                detailGalleryPracticeFragment = DetailGalleryPracticeFragment()
                val bundle = Bundle().apply {
                    putString("letter_name", galleryItem.letterName)
                }
                detailGalleryPracticeFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detailGalleryPracticeFragment)
                    .addToBackStack(null)
                    .commit()
            }

            ViewCompat.setLayoutDirection(rvGalleryPractice, ViewCompat.LAYOUT_DIRECTION_RTL)
            rvGalleryPractice.layoutManager = GridLayoutManager(requireContext(), 5)
            rvGalleryPractice.adapter = adapter

            binding.icBack.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LessonGalleryFragment())
                    .addToBackStack(null)
                    .commit()
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LessonGalleryFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    private fun createGalleryItems(drawings: List<Drawing>): List<GalleryItem> {
        val letterNames = listOf(
            "Alif", "Ba", "Ta", "Tsa", "Jim", "Kha", "Kho", "Dal", "Dzal", "Ra", "Za",
            "Sin", "Syin", "Shod", "Dhod", "Tho", "Tzo", "Ain", "Ghain", "Fa", "Qaf",
            "Kaf", "Lam", "Mim", "Nun", "Wau", "Ha", "Lam Alif", "Hamzah", "Ya"
        )

        return letterNames.map { letterName ->
            // Cari gambar berdasarkan letterName dan userId
            val drawing = drawings.find { it.letterName == letterName }
            val imagePath = drawing?.imagePath  // Ambil image path jika ada

            // Log untuk memeriksa imagePath
            Log.d("PracticeGalleryFragment", "Letter: $letterName, ImagePath: $imagePath")

            GalleryItem(imagePath, letterName)
        }
    }
}



