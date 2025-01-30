package com.example.TugasAkhir.qolami.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentRegisterBinding
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var loginFragment: LoginFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnRegister.setOnClickListener {
            val fullname = binding.etFullname.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confPassword = binding.etConfirmPassword.text.toString().trim()

            // Validasi Input
            when {
                fullname.isEmpty() || fullname.length < 3 -> {
                    binding.etFullname.error = "Nama lengkap harus diisi (minimal 3 karakter)."
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = "Email tidak valid."
                }
                email.isEmpty() ->{
                    binding.etEmail.error = "Email tidak boleh kosong."
                }
                password.length < 8 -> {
                    binding.etPassword.error = "Password minimal 8 karakter."
                }
                password != confPassword -> {
                    binding.etConfirmPassword.error = "Password tidak cocok."
                }
                else -> {
                    // Semua validasi lolos
                    viewModel.register(fullname, email, password) { success, errorMessage ->
                        if (success) {
                            // Berhasil, navigasi ke login
                            loginFragment = LoginFragment()
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                                )
                                .replace(R.id.fragmentContainer, loginFragment)
                                .addToBackStack(null)
                                .commit()
                        } else {
                            // Tampilkan pesan error
                            showDialogAuth(errorMessage!!)
                        }
                    }
                }
            }
        }

        binding.btnToLogin.setOnClickListener {
            loginFragment= LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,loginFragment)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun showDialogAuth(message: String){
        val dialogview=layoutInflater.inflate(R.layout.dialog_auth,null)
        val messageDialog=dialogview.findViewById<TextView>(R.id.dialog_auth_message)
        val button=dialogview.findViewById<TextView>(R.id.dialog_auth_button)
        dialogview.setBackgroundResource(R.drawable.rounded_blue)

        messageDialog.text=message
        val dialogAuth= AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setView(dialogview)
            .setCancelable(false)
            .create()

        button.setOnClickListener {
            dialogAuth.dismiss()
        }
        dialogAuth.show()

    }

}

