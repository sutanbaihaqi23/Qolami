package com.example.TugasAkhir.qolami.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R

class GalleryLessonAdapter : RecyclerView.Adapter<GalleryLessonAdapter.GalleryLessonViewHolder>() {
    private var mediaPlayer: MediaPlayer? = null

    inner class GalleryLessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.img_pelajaran)

        fun bind(position: Int) {
            imageView.setImageResource(hijaiyahImages[position])

            itemView.setOnClickListener {
                playSound(position)
            }
        }

        private fun playSound(position: Int) {
            mediaPlayer?.release()

            val soundResourceId = itemView.context.resources.getIdentifier(
                "sound${position + 1}",
                "raw",
                itemView.context.packageName
            )

            mediaPlayer = MediaPlayer.create(itemView.context, soundResourceId).apply {
                setOnCompletionListener { mp -> mp.release() }
                start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryLessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson_gallery, parent, false)
        return GalleryLessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryLessonViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = hijaiyahImages.size

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private val hijaiyahImages = listOf(
            R.drawable.alif,
            R.drawable.ba,
            R.drawable.ta,
            R.drawable.sa,
            R.drawable.jim,
            R.drawable.kha,
            R.drawable.kho,
            R.drawable.dal,
            R.drawable.dzal,
            R.drawable.ra,
            R.drawable.za,
            R.drawable.sin,
            R.drawable.syin,
            R.drawable.shod,
            R.drawable.dzo,
            R.drawable.tho,
            R.drawable.tzo,
            R.drawable.ain,
            R.drawable.goin,
            R.drawable.fa,
            R.drawable.kof,
            R.drawable.kaf,
            R.drawable.lam,
            R.drawable.mim,
            R.drawable.nun,
            R.drawable.waw,
            R.drawable.ha,
            R.drawable.lamalif,
            R.drawable.hamzah,
            R.drawable.ya,

            // Tambahkan sisa gambar hijaiyah
        )
    }
}