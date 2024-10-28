package com.example.TugasAkhir.qolami.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentRegisterBinding
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import kotlin.math.log

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

        binding.btnRegister.setOnClickListener {
            val fullname = binding.etFullname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confPassword= binding.etConfirmPassword.text.toString()

            if (fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()&&confPassword.isNotEmpty()){
                if (password == confPassword){
                    viewModel.register(fullname, email, password) { success ->
                        if (success) {
                            loginFragment=LoginFragment()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer,loginFragment)
                                .addToBackStack(null)
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), "Login failed, please try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Password doesn't match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnToLogin.setOnClickListener {
            loginFragment=LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,loginFragment)
                .addToBackStack(null)
                .commit()

        }
    }
}
