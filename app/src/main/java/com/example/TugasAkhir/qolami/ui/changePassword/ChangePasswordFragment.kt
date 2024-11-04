package com.example.TugasAkhir.qolami.ui.changePassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.databinding.FragmentChangePasswordBinding
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel


class ChangePasswordFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangePassword.setOnClickListener {
            val oldPassword = binding.etOldPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()

            viewModel.changePassword(oldPassword, newPassword) { success, message ->
                if (success) {
                    Toast.makeText(context, "Password successfully changed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to change password: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}