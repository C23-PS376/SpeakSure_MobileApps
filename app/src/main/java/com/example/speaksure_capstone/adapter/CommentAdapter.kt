package com.example.speaksure_capstone.adapter

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.databinding.ItemCommentBinding
import com.example.speaksure_capstone.response.ThreadData
import java.io.IOException


class CommentAdapter :PagingDataAdapter<ThreadData, CommentAdapter.MyViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }


    class MyViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ThreadData) {
            var isPlaying = false
            var mediaPlayer: MediaPlayer? = null
            binding.Name.text = data.text
            binding.comment.text = data.text
            binding.btnPlayThread.setOnClickListener {
                if(!isPlaying){
                    isPlaying =true
                    mediaPlayer?.apply {
                        stop()
                        release()
                    }
                    mediaPlayer = null
                    mediaPlayer = MediaPlayer().apply {
                        try {
                            setDataSource(data.audio.toString())
                            prepare()
                            start()
                        } catch (e: IOException) {
                        }
                    }
                }
                else{
                    isPlaying =false
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            }



        }
    }


}

object DiffCallback : DiffUtil.ItemCallback<ThreadData>() {
    override fun areItemsTheSame(oldItem: ThreadData, newItem: ThreadData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ThreadData, newItem: ThreadData): Boolean {
        return oldItem == newItem
    }
}
