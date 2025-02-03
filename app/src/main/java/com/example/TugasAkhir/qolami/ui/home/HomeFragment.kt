package com.example.TugasAkhir.qolami.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentHomeBinding
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.latihan.ListLatihanFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import com.example.TugasAkhir.qolami.ui.setting.SettingsFragment
import com.example.TugasAkhir.qolami.ui.test.WritingTestHomeFragment
import com.example.TugasAkhir.qolami.util.NetworkUtils
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var loginFragment: LoginFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var lessonFragment: ListLatihanFragment
    private lateinit var writingTestFragment: WritingTestHomeFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel.getFullname { fullname ->
            if (isAdded) { // Pastikan fragment aktif
                binding.tvHome.text = fullname ?: "Nama tidak tersedia"
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Jangan lakukan apa-apa untuk menonaktifkan tombol back
                    requireActivity().finish()
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginFragment = LoginFragment()
        settingsFragment = SettingsFragment()
        lessonFragment = ListLatihanFragment()
        writingTestFragment = WritingTestHomeFragment()


        binding.icToSetting.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, settingsFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnLesson.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, lessonFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.btnTest.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, writingTestFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
