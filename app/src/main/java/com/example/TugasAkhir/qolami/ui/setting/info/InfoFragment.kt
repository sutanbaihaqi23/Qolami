package com.example.TugasAkhir.qolami.ui.setting.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.FragmentInfoBinding


class InfoFragment : Fragment() {

   private lateinit var binding: FragmentInfoBinding
   private val youtubechannel="https://www.youtube.com/@qolamichanel9019"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        binding.btnBackToSettings.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.icYoutube.setOnClickListener {
            openYouTubeChannel(youtubechannel)
        }
        return binding.root
    }

    private fun openYouTubeChannel(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }


}