package com.example.TugasAkhir.qolami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.HurufModel

class PelajaranAdapter(
    private val hurufList: List<HurufModel>,
    private val onItemClick: (HurufModel) -> Unit
) : RecyclerView.Adapter<PelajaranAdapter.HurufViewHolder>() {

    inner class HurufViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPelajaran: ImageView = itemView.findViewById(R.id.img_pelajaran)

        fun bind(huruf: HurufModel) {
            imgPelajaran.setImageResource(huruf.imageResource)
            itemView.setOnClickListener {
                onItemClick(huruf)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HurufViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelajaran, parent, false)
        return HurufViewHolder(view)
    }

    override fun onBindViewHolder(holder: HurufViewHolder, position: Int) {
        holder.bind(hurufList[position])
    }

    override fun getItemCount(): Int = hurufList.size
}
