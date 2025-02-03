package com.example.TugasAkhir.qolami.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.ActivityMainBinding
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.auth.RegisterFragment
import com.example.TugasAkhir.qolami.ui.home.HomeFragment
import com.example.TugasAkhir.qolami.util.NetworkUtils
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkUtils: NetworkUtils
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        // Inisialisasi NetworkUtils
        networkUtils = NetworkUtils(this)
        networkUtils.registerNetworkCallback()

        // Pantau perubahan koneksi internet
        networkUtils.isConnected.observe(this, Observer { isConnected ->
            if (!isConnected) {
                showCustomSnackbar("Tidak Terhubung dengan Internet!")
                binding.fragmentContainer.visibility = View.GONE
                Handler().postDelayed({
                    finish()
                }, 3000)
            } else {
                showCustomSnackbar("Terhubung dengan Internet!")
                binding.fragmentContainer.visibility = View.VISIBLE
            }
        })

        handleNavigationIntent(savedInstanceState)

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

    override fun onDestroy() {
        super.onDestroy()

        networkUtils.unregisterNetworkCallback()
    }

    private fun handleNavigationIntent(savedInstanceState: Bundle?) {
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
        val snackbarView = LayoutInflater.from(this).inflate(R.layout.snackbar_galeri, null)
        val messageTextView = snackbarView.findViewById<TextView>(R.id.snackbar_message)
        messageTextView.text = message
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()
    }
}