package com.example.TugasAkhir.qolami.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentSettingsBinding
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import com.example.TugasAkhir.qolami.ui.setting.changePassword.ChangePasswordFragment
import com.example.TugasAkhir.qolami.ui.setting.info.InfoFragment


class SettingsFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var changePasswordFragment: ChangePasswordFragment
    private lateinit var loginFragment: LoginFragment
    private lateinit var infoFragment: InfoFragment



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding= FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePasswordFragment = ChangePasswordFragment()
        loginFragment = LoginFragment()
        infoFragment = InfoFragment()

        binding.icBackSettingToHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnInfo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,infoFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,loginFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnChangePassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer,changePasswordFragment)
                .addToBackStack(null)
                .commit()
        }
    }

}