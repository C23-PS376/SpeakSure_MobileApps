package com.example.speaksure_capstone.ui.detail

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.databinding.ActivityDetailBinding
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.DetailResponse
import com.example.speaksure_capstone.response.LikeResponse
import com.example.speaksure_capstone.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.Timestamp
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityDetailBinding
    private var isPlaying = false
    private lateinit var viewModel: DetailViewModel
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val ID_THREAD = "id_thread"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = this.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var token = preferences.getString(LoginActivity.TOKEN, "").toString()
        token= "Bearer $token"

        if(token == null){
            val intent = Intent(this@DetailActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val id = intent.getStringExtra(ID_THREAD).toString()
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val detailThread = intent.getStringExtra(ID_THREAD)

        if (detailThread!= null) {
            viewModel.getDetail(token,id)
        }
        viewModel.detailThread.observe(this) { detailThreads ->
            setDetailThread(detailThreads)
        }
        mediaPlayer = MediaPlayer()
    }

    private fun setDetailThread(detail: DetailResponse) {
        var likesCount: Int = detail.data?.likesCount!!.toInt()
        binding.Name.text = detail.data?.user?.name
        val timeStamp = detail.data?.createdAt?.let { Timestamp(it.toLong()) }
        binding.dateThread.text = timeStamp.toString()
        binding.description.text = detail.data?.description
        binding.title.text = detail.data?.title
        binding.tvJlhLike.text = detail.data?.likesCount
        binding.tvJlhComment.text = detail.data?.commentsCount
        binding.btnPlayThread.text = detail.data?.audioLength
        Glide.with(this@DetailActivity)
            .load(detail.data?.image)
            .into(binding.ivThread)
        binding.btnPlayThread.setOnClickListener {
            if(!isPlaying){
                playAudio(detail.data?.audio.toString())
            }
            else{
                pauseAudio()
            }
        }
        binding.btnUp.setOnClickListener {
            preferences =binding.root.context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            var token = preferences.getString(LoginActivity.TOKEN, "").toString()
            token= "Bearer $token"
            val id = detail?.data.id.toString()
            val client = ApiConfig.getApiService().like(token,id)
            client.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                    if (response.isSuccessful && response.body()?.statusCode == 201) {
                        Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                        likesCount++ // Tambahkan 1 ke likesCount
                        binding.tvJlhLike.text = likesCount.toString()
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
            val id = detail?.data.id.toString()
            val client = ApiConfig.getApiService().unlike(token,id)
            client.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                        likesCount-- // Tambahkan 1 ke likesCount
                        binding.tvJlhLike.text = likesCount.toString()
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

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun playAudio(data:String){
        isPlaying =true
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(data)
                prepare()
                start()
                Toast.makeText(this@DetailActivity, "Playing Audio", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
            }
        }
    }

    private fun pauseAudio(){
        isPlaying =false
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(this@DetailActivity, "Stop Audio", Toast.LENGTH_SHORT)
            .show()
    }
}