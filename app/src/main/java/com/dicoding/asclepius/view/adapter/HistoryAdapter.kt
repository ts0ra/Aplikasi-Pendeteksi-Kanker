package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.database.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.io.File

class HistoryAdapter(
    private val listener: OnItemButtonClickListener
) : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    interface OnItemButtonClickListener {
        fun onItemButtonClick(history: HistoryEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, listener)
    }

    class MyViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity, listener: OnItemButtonClickListener){
            binding.historyScore.text = history.score
            binding.historyResult.text = history.result
            Glide
                .with(binding.root.context)
                .load(File(history.imagePath))
                .into(binding.historyImg)
            binding.historyDeleteBtn.setOnClickListener {
                listener.onItemButtonClick(history)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

}