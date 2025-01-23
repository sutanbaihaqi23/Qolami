package com.example.TugasAkhir.qolami.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.databinding.ItemCircleNumberBinding

class QuestionAdapter(
    private val totalQuestions: Int,
    private var currentQuestion: Int
) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    // Fungsi untuk memperbarui nomor soal saat ini
    fun updateCurrentQuestion(newCurrent: Int) {
        currentQuestion = newCurrent
        notifyDataSetChanged() // Memberitahu adapter untuk memperbarui semua item
    }

    inner class ViewHolder(private val binding: ItemCircleNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Fungsi bind untuk menyesuaikan tampilan berdasarkan kondisi nomor soal
        fun bind(position: Int) {
            val questionNumber = position + 1
            binding.numberTextView.text = questionNumber.toString()

            val backgroundDrawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                cornerRadius = 50f // Rounded circle
                when {
                    questionNumber < currentQuestion + 1 -> { // Passed question
                        setColor(android.graphics.Color.WHITE) // Background color
                        setStroke(4, android.graphics.Color.parseColor("#32B8FF")) // Stroke color
                        binding.numberTextView.setTextColor(android.graphics.Color.parseColor("#32B8FF"))
                        binding.numberTextView.alpha = 1f
                    }
                    questionNumber == currentQuestion + 1 -> { // Current question
                        setColor(android.graphics.Color.parseColor("#32B8FF")) // Background color
                        setStroke(0, android.graphics.Color.TRANSPARENT) // No stroke
                        binding.numberTextView.setTextColor(android.graphics.Color.WHITE)
                        binding.numberTextView.alpha = 1f
                    }
                    else -> { // Upcoming question
                        setColor(android.graphics.Color.WHITE) // Background color
                        setStroke(4, android.graphics.Color.parseColor("#32B8FF")) // Stroke color
                        binding.numberTextView.setTextColor(android.graphics.Color.parseColor("#32B8FF"))
                        binding.numberTextView.alpha = 0.3f
                    }
                }
            }
            binding.numberTextView.background = backgroundDrawable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCircleNumberBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position) // Panggil bind untuk setiap item
    }

    override fun getItemCount(): Int = totalQuestions // Total jumlah soal
}