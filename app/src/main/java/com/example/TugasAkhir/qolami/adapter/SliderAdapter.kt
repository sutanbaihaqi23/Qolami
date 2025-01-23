package com.example.TugasAkhir.qolami.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.SliderItem
import com.example.TugasAkhir.qolami.databinding.ItemSliderBinding

class SliderAdapter(private val context: Context) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val slides = listOf(
        SliderItem(
            "QOLAMI",
            "with method!",
            R.drawable.qolami_logo
        ),
        SliderItem(
            "QOLAMI",
            "with method!",
            R.drawable.qolami_logo
        ),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount() = slides.size

    inner class SliderViewHolder(private val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SliderItem) {
            binding.apply {
                logoImage.setImageResource(item.imageRes)
            }
        }
    }
}