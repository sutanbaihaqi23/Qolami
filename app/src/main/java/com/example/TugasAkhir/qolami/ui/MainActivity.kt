package com.example.TugasAkhir.qolami.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }

        if (viewModel.isLoggedIn()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }
    }
}