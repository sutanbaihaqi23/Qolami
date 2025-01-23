package com.example.TugasAkhir.qolami.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.auth.RegisterFragment
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleNavigationIntent(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }

        if (viewModel.isLoggedIn()) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }
    }
    private fun handleNavigationIntent(savedInstanceState: Bundle?) {
        // Only handle navigation if this is a new instance
        if (savedInstanceState == null) {
            when (intent.getStringExtra("navigate_to")) {
                "login" -> showLoginFragment()
                "register" -> showRegisterFragment()
            }
        }
    }

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, LoginFragment())
            .commit()
    }

    private fun showRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, RegisterFragment())
            .commit()
    }
}