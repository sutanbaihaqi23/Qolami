package com.example.TugasAkhir.qolami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.GalleryItem

class GalleryPracticeAdapter(
    private val galleryItems: List<GalleryItem>,
    private val onItemClick: (GalleryItem) -> Unit
) : RecyclerView.Adapter<GalleryPracticeAdapter.GalleryPracticeViewHolder>() {

    class GalleryPracticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_gallery_practice)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): GalleryPracticeViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_practice, parent, false)
        return GalleryPracticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryPracticeViewHolder, position: Int) {
        val galleryItem = galleryItems[position]
        holder.imageView.setImageResource(galleryItem.imagesGallery)
        holder.itemView.setOnClickListener {
            onItemClick(galleryItem)
        }
    }

    override fun getItemCount() = galleryItems.size

}