package com.example.TugasAkhir.qolami.ui.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentWritingTestHomeBinding
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.ui.latihan.ListLatihanFragment
import com.example.TugasAkhir.qolami.ui.test.history.ListHistoryFragment


class WritingTestHomeFragment : Fragment() {
    private lateinit var binding: FragmentWritingTestHomeBinding
    private lateinit var testFragment: TestFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var listHistoryFragment: ListHistoryFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWritingTestHomeBinding.inflate(inflater, container, false)
        testFragment=TestFragment()
        homeFragment= HomeFragment()
        listHistoryFragment= ListHistoryFragment()



        binding.btnBackToHome.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer,homeFragment).commit()
        }

        binding.btnHistoryExam.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer,listHistoryFragment).commit()

        }
        binding.navIcPractice.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListLatihanFragment())
                .addToBackStack(null)
                .commit()

        }

        binding.btnStartExam.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,testFragment)
                .addToBackStack(null)
                .commit()


        }
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