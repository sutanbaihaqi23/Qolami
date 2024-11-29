package com.example.TugasAkhir.qolami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.LessonItem

class LessonAdapter(
    private val lessons: List<LessonItem>,
    private val onItemClick: (LessonItem) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lessonName: TextView = view.findViewById(R.id.tv_lesson)
        val img1: ImageView = view.findViewById(R.id.iv_img_1)
        val img2: ImageView = view.findViewById(R.id.iv_img_2)
        val img3: ImageView = view.findViewById(R.id.iv_img_3)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.lessonName.text = lesson.name

        val images = lesson.images
        if (images.isNotEmpty()) {
            holder.img1.setImageResource(images[0])
            holder.img2.setImageResource(images.getOrNull(1) ?: R.drawable.alif)
            holder.img3.setImageResource(images.getOrNull(2) ?: R.drawable.alif)
        }

        // Handle click event
        holder.itemView.setOnClickListener {
            onItemClick(lesson)
        }
    }

    override fun getItemCount() = lessons.size
}
