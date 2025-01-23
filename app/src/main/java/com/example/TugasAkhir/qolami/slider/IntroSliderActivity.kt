package com.example.TugasAkhir.qolami.slider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.SliderAdapter
import com.example.TugasAkhir.qolami.databinding.ActivityIntroSliderBinding
import com.example.TugasAkhir.qolami.ui.MainActivity
import com.example.TugasAkhir.qolami.ui.auth.LoginFragment
import com.example.TugasAkhir.qolami.ui.auth.RegisterFragment

class IntroSliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroSliderBinding
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSlider()
        setupButtons()
    }

    private fun setupSlider() {
        sliderAdapter = SliderAdapter(this)
        binding.viewPager.adapter = sliderAdapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateButtonVisibility(position)
            }
        })
    }

    private fun updateButtonVisibility(position: Int) {
        if (position == sliderAdapter.itemCount - 1) {
            binding.nextButton.visibility = View.GONE
            binding.loginContainer.visibility = View.VISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.loginContainer.visibility = View.GONE
        }
    }

    private fun setupButtons() {
        binding.nextButton.setOnClickListener {
            binding.viewPager.currentItem += 1
        }

        binding.loginButton.setOnClickListener {
            navigateToMainWithFragment("login")
        }
    }

    private fun navigateToMainWithFragment(destination: String) {
        setIntroShown()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to", destination)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun setIntroShown() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("intro_shown", true).apply()
    }
}