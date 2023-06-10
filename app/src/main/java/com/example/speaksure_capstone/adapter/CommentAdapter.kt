package com.example.speaksure_capstone.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.speaksure_capstone.databinding.ItemCommentBinding
import com.example.speaksure_capstone.response.CommentItem
import java.io.IOException
import java.sql.Timestamp


class CommentAdapter :PagingDataAdapter<CommentItem, CommentAdapter.MyViewHolder>(DiffCallback){

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
        fun bind(data: CommentItem) {
            var isPlaying = false
            var mediaPlayer: MediaPlayer? = null
            val timeStamp = Timestamp(data.createdAt.toLong())
            binding.Name.text = data.text
            binding.comment.text = data.text
            binding.dateThread.text = timeStamp.toString()
            binding.btnPlayThread.text = data.audioLength.toString()
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

object DiffCallback : DiffUtil.ItemCallback<CommentItem>() {
    override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
        return oldItem == newItem
    }
}
