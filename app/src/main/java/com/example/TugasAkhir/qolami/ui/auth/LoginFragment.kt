package com.example.TugasAkhir.qolami.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider

import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentLoginBinding
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel

class LoginFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var registerFragment: RegisterFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }



        // Handle tombol back dari perangkat


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showDialogAuth("Email atau Kata Sandi Kosong")
            } else {
                viewModel.login(email, password) { success, message ->
                    if (success) {
                        // Arahkan ke HomeFragment jika login berhasil
                        homeFragment = HomeFragment()
                        parentFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragmentContainer, homeFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        // Tampilkan dialog error jika login gagal
                        showDialogAuth(message!!)
                    }
                }
            }
        }



        binding.btnToRegister.setOnClickListener {
           registerFragment= RegisterFragment()
           parentFragmentManager.beginTransaction()
               .replace(R.id.fragmentContainer,registerFragment)
               .addToBackStack(null)
               .commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }


    private fun showDialogAuth(message: String){
        val dialogview=layoutInflater.inflate(R.layout.dialog_auth,null)
        val messageDialog=dialogview.findViewById<TextView>(R.id.dialog_auth_message)
        val button=dialogview.findViewById<TextView>(R.id.dialog_auth_button)
        dialogview.setBackgroundResource(R.drawable.rounded_blue)

        messageDialog.text=message
        val dialogAuth=AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setView(dialogview)
            .setCancelable(false)
            .create()

        button.setOnClickListener {
            dialogAuth.dismiss()
        }
        dialogAuth.show()

    }
}
