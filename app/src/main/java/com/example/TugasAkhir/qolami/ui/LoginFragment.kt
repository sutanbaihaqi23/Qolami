package com.example.TugasAkhir.qolami.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.fragment.findNavController
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentLoginBinding
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password) { success ->
                if (success) {
                    homeFragment= HomeFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer,homeFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Login failed, please try again", Toast.LENGTH_SHORT).show()
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
    }
}
