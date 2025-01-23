package com.example.TugasAkhir.qolami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.ListHistoryItem
import com.example.TugasAkhir.qolami.databinding.ItemListHistoryBinding

class ListHistoryAdapter(private val items: List<ListHistoryItem>, private val onClick: (ListHistoryItem) -> Unit) : RecyclerView.Adapter<ListHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListHistoryItem) {
            binding.tvHistoryNumber.text =  "Attempt to : "+item.historyNumber.toString()
            binding.tvFinished.text = "Finished : " +item.finishedNumber
            binding.tvScore.text = "Score : "+item.score.toString()

            // Set click listener
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}

