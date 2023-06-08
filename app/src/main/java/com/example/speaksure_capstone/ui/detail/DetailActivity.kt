package com.example.speaksure_capstone.ui.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.databinding.ActivityDetailBinding
import com.example.speaksure_capstone.response.DetailResponse
import com.example.speaksure_capstone.ui.login.LoginActivity
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