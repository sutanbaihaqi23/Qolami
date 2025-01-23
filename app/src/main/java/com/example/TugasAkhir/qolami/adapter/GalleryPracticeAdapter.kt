package com.example.TugasAkhir.qolami.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.GalleryItem
import com.example.TugasAkhir.qolami.databinding.ItemGalleryPracticeBinding
import java.io.File

class GalleryPracticeAdapter(
    private val items: List<GalleryItem>,
    private val onItemClick: (GalleryItem) -> Unit
) : RecyclerView.Adapter<GalleryPracticeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGalleryPracticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem) {
            // Log untuk memeriksa imagePath
            Log.d("GalleryPracticeAdapter", "ImagePath: ${galleryItem.imagePath}")

            if (galleryItem.imagePath != null) {
                val imgFile = File(galleryItem.imagePath)
                if (imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    if (bitmap != null) {
                        binding.imgGalleryPractice.setImageBitmap(bitmap)
                        binding.imgGalleryPractice.imageTintMode=null
                    } else {
                        Log.e("GalleryPracticeAdapter", "Gagal memuat gambar dari path: ${imgFile.absolutePath}")
                        binding.imgGalleryPractice.setImageResource(R.drawable.defaultimg)
                        binding.imgGalleryPractice.imageTintMode=null
                    }
                } else {
                    Log.d("GalleryPracticeAdapter", "Gambar tidak ditemukan di path: ${imgFile.absolutePath}")
                    binding.imgGalleryPractice.setImageResource(R.drawable.defaultimg)
                    binding.imgGalleryPractice.imageTintMode=null
                }
            } else {
                binding.imgGalleryPractice.setImageResource(R.drawable.defaultimg)
                binding.imgGalleryPractice.imageTintMode=null
            }

            binding.root.setOnClickListener {
                onItemClick(galleryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryPracticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val galleryItem = items[position]
        holder.bind(galleryItem)
    }
    override fun getItemCount() = items.size
}

