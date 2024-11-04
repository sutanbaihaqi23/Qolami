package com.example.TugasAkhir.qolami.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentHomeBinding
import com.example.TugasAkhir.qolami.ui.changePassword.ChangePasswordFragment
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.pelajaran.PelajaranFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var loginFragment: LoginFragment
    private lateinit var changePasswordFragment: ChangePasswordFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginFragment= LoginFragment()
        viewModel.getFullname { fullname ->
            binding.tvHome.text = fullname ?: "Nama tidak tersedia"
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,loginFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.btnToChangePassword.setOnClickListener {
            changePasswordFragment = ChangePasswordFragment()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,changePasswordFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnPelajaran.setOnClickListener {
            val pelajaranFragment = PelajaranFragment()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, pelajaranFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
