package com.example.TugasAkhir.qolami

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.TugasAkhir.qolami.databinding.ActivitySplashScreenBinding
import com.example.TugasAkhir.qolami.slider.IntroSliderActivity
import com.example.TugasAkhir.qolami.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({

            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val introShown = prefs.getBoolean("intro_shown", false)
            //val intent=Intent(this, IntroSliderActivity::class.java)
            val intent = if (introShown) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, IntroSliderActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }
}