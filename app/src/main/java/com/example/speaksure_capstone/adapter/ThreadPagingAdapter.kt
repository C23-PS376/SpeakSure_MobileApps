package com.example.speaksure_capstone.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.databinding.ItemThreadBinding
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.LikeResponse
import com.example.speaksure_capstone.response.ListThreads
import com.example.speaksure_capstone.response.LoginRegisterResponse
import com.example.speaksure_capstone.ui.detail.DetailActivity
import com.example.speaksure_capstone.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.Timestamp
import java.util.*

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
            var likesCount: Int = data.likesCount.toInt()
            var preferences: SharedPreferences
            var isPlaying = false
            var mediaPlayer: MediaPlayer? = null
            Glide.with(itemView)
                .load(data.image)
                .into(binding.imageThread)
            val timeStamp = Timestamp(data.createdAt.toLong())
            binding.dateThread.text = timeStamp.toString()
            binding.title.text = data.title
            binding.Name.text = data.user?.name
            binding.jlhLike.text = data.likesCount
            binding.jlhComment.text = data.commentsCount
            binding.btnPlayThread.text = data.audioLength
            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, DetailActivity::class.java )
                intent.putExtra(DetailActivity.ID_THREAD, data.id)
                binding.root.context.startActivity(intent)
            }
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
            binding.btnUp.setOnClickListener {
                preferences =binding.root.context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                var token = preferences.getString(LoginActivity.TOKEN, "").toString()
                token= "Bearer $token"
                val id = data.id
                val client = ApiConfig.getApiService().like(token,id)
                client.enqueue(object : Callback<LikeResponse> {
                    override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                        if (response.isSuccessful && response.body()?.statusCode == 201) {
                            Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                            likesCount++ // Tambahkan 1 ke likesCount
                            binding.jlhLike.text = likesCount.toString()
                        } else {
                            Toast.makeText(binding.root.context,"Thread already liked", Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                        Toast.makeText(binding.root.context,t.message, Toast.LENGTH_SHORT).show()
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    }
                })
            }
            binding.btnDown.setOnClickListener {
                preferences =binding.root.context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                var token = preferences.getString(LoginActivity.TOKEN, "").toString()
                token= "Bearer $token"
                val id = data.id
                val client = ApiConfig.getApiService().unlike(token,id)
                client.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                            likesCount-- // Tambahkan 1 ke likesCount
                            binding.jlhLike.text = likesCount.toString()
                        } else {
                            Toast.makeText(binding.root.context,"Thread already unliked", Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(binding.root.context,t.message, Toast.LENGTH_SHORT).show()
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    }
                })
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