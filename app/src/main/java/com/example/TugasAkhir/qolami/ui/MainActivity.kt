package com.example.TugasAkhir.qolami.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.ActivityMainBinding
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.auth.RegisterFragment
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.util.NetworkUtils
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleNavigationIntent(savedInstanceState)


        if (NetworkUtils.isInternetAvailable(this)){
            showCustomSnackbar("Terhubung dengan Internet !")
        }else{
            showCustomSnackbar("Tidak Terhubung dengan Internet !")
            binding.fragmentContainer.visibility = View.GONE
            val handler = android.os.Handler()
            handler.postDelayed({
                finish()
            }, 3000)
        }





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

    @SuppressLint("RestrictedApi")
    private fun showCustomSnackbar(message: String) {
        // Inflate custom layout
        val snackbarView = LayoutInflater.from(this).inflate(R.layout.snackbar_galeri, null)

        // Set the message
        val messageTextView = snackbarView.findViewById<TextView>(R.id.snackbar_message)
        messageTextView.text = message

        // Create Snackbar
        val rootView: View = findViewById(android.R.id.content) // Menggunakan root view activity
        val snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        // Remove default padding and background
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

        // Add custom view to Snackbar
        snackbarLayout.addView(snackbarView, 0)
        // Show Snackbar
        snackbar.show()
    }
}