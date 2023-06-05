package com.example.speaksure_capstone.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.databinding.ItemThreadBinding
import com.example.speaksure_capstone.response.ListThreads
import com.example.speaksure_capstone.ui.detail.DetailActivity

class ThreadPagingAdapter: PagingDataAdapter<ListThreads, ThreadPagingAdapter.MyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemThreadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemThreadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListThreads) {
            Glide.with(itemView)
                .load(data.image)
                .into(binding.imageThread)
            binding.dateThread.text = data.createdAt
            binding.description.text = data.description
            binding.Name.text = data.user?.name
            binding.jlhLike.text = data.likesCount
            binding.jlhComment.text = data.commentsCount
            binding.btnPlayThread.text = data.audioLength
            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, DetailActivity::class.java )
                intent.putExtra(DetailActivity.ID_THREAD, data.id)
                binding.root.context.startActivity(intent)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ListThreads>() {
        override fun areItemsTheSame(oldItem: ListThreads, newItem: ListThreads): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListThreads, newItem: ListThreads): Boolean {
            return oldItem == newItem
        }
    }


}