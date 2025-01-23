package com.example.TugasAkhir.qolami.ui.test

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.ResultTestAdapter
import com.example.TugasAkhir.qolami.data.TempResultTest
import com.example.TugasAkhir.qolami.data.TempResultTest2
import com.example.TugasAkhir.qolami.data.TestResult
import com.example.TugasAkhir.qolami.database.AppDatabase
import com.example.TugasAkhir.qolami.databinding.FragmentResultTestBinding
import com.example.TugasAkhir.qolami.ui.latihan.ListLatihanFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class ResultTestFragment : Fragment() {
    private lateinit var binding: FragmentResultTestBinding
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var results = mutableListOf<TempResultTest>()
    private var results2 = mutableListOf<TempResultTest2>()
    private lateinit var writingTestHomeFragment: WritingTestHomeFragment
    private lateinit var authViewModel: AuthViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentResultTestBinding.inflate(inflater, container, false)
        writingTestHomeFragment= WritingTestHomeFragment()
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]


        binding.navIcPractice.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListLatihanFragment())
                .addToBackStack(null)
                .commit()
        }
        arguments?.let {
            correctAnswers = it.getInt("correctAnswers", 0)
            incorrectAnswers = it.getInt("incorrectAnswers", 0)

            results = it.getParcelableArrayList<TempResultTest>("results") ?: arrayListOf()
            results2 = it.getParcelableArrayList<TempResultTest2>("results2") ?: arrayListOf()
        }

        val total = correctAnswers + incorrectAnswers
        binding.tvFinished.text = String.format("%d / 10", total)
        binding.tvScore.text = String.format("%d%%", correctAnswers * 10)
        val totalfinishedString = String.format("%d / 10", total)





        binding.btnSaveResultTest.setOnClickListener {
            val getUserUid = authViewModel.getUserUid()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val database = AppDatabase.getDatabase(requireContext())

                    // Mengambil jumlah history yang sudah ada dari user
                    val currentHistoryCount = database.testResultDao().getHistoryCountForUser(
                        getUserUid.toString()
                    )

                    // Tentukan historyNumber berdasarkan jumlah history yang ada
                    Log.d("HistoryCount", "Current History Count: $currentHistoryCount")
                    val historyNumber = currentHistoryCount + 1
                    Log.d("HistoryCount", "Calculated History Number: $historyNumber")


                    // Membuat hasil untuk results dan results2
                    val testResults = mutableListOf<TestResult>()

                    // Memproses results
                    testResults.addAll(results.map {
                        TestResult(
                            userId = getUserUid,
                            bitmapDrawingView = convertBitmapToByteArray(it.bitmapDrawingView),
                            confidence = it.confidence,
                            predictedLabel = it.predictedLabel,
                            letter = it.letter,
                            leftLetter = null,
                            middleLetter = null,
                            rightLetter = null,
                            leftLetterImg = null,
                            middleLetterImg = null,
                            rightLetterImg = null,
                            numberTest = it.numberTest,
                            isCorrect = it.isCorrect,
                            historyNumber = historyNumber, // Pastikan historyNumber konsisten
                            finishedNumber = totalfinishedString,
                            score = String.format("%d%%", correctAnswers * 10),
                            type = 0
                        )
                    })

                    // Memproses results2
                    testResults.addAll(results2.map {
                        TestResult(
                            userId = getUserUid,
                            bitmapDrawingView = convertBitmapToByteArray(it.bitmapDrawingView),
                            confidence = it.confidence,
                            predictedLabel = it.predictedLabel,
                            letter = null,
                            leftLetter = it.leftLetter,
                            middleLetter = it.middleLetter,
                            rightLetter = it.rightLetter,
                            leftLetterImg = it.leftLetterImg,
                            middleLetterImg = it.middleLetterImg,
                            rightLetterImg = it.rightLetterImg,
                            numberTest = it.numberTest,
                            isCorrect = it.isCorrect,
                            historyNumber = historyNumber, // Pastikan historyNumber konsisten
                            finishedNumber = totalfinishedString,
                            score = String.format("%d%%", correctAnswers * 10),
                            type = 1
                        )
                    })

                    // Menyimpan hasil ke database
                    testResults.forEach { testResult ->
                        database.testResultDao().insertTestResult(testResult)
                    }

                    Toast.makeText(requireContext(), "Results saved successfully!", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, writingTestHomeFragment)
                        .addToBackStack(null)
                        .commit()

                } catch (e: Exception) {
                    Log.e("ResultTestFragment", "Error saving results: ${e.message}")
                    Toast.makeText(requireContext(), "Failed to save results: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }




        binding.btnRestartTest.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, writingTestHomeFragment)
                .addToBackStack(null)
                .commit()
        }

        val resultList :List <Parcelable> = results + results2
        val adapter = ResultTestAdapter(resultList)
        binding.rvResultTest.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResultTest.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle tombol back dari perangkat
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, writingTestHomeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

}
