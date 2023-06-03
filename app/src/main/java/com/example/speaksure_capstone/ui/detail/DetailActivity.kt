package com.example.speaksure_capstone.ui.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.ActivityDetailBinding
import com.example.speaksure_capstone.databinding.ActivityLoginBinding
import com.example.speaksure_capstone.response.DetailResponse
import com.example.speaksure_capstone.response.ProfileResponse
import com.example.speaksure_capstone.ui.login.LoginActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

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

        val id = intent.getStringExtra(ID_THREAD).toString()
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val detailThread = intent.getStringExtra(ID_THREAD)

        if (detailThread!= null) {
            viewModel.getDetail(token,id)
        }
        viewModel.detailThread.observe(this) { detailThreads ->
            setDetailThread(detailThreads)
        }
    }

    private fun setDetailThread(detail: DetailResponse) {
        binding.Name.text = detail.data?.user?.name
        binding.dateThread.text = detail.data?.createdAt
        binding.description.text = detail.data?.description
        binding.tvJlhLike.text = detail.data?.likesCount
        binding.tvJlhComment.text = detail.data?.commentsCount
        binding.btnPlayThread.text = detail.data?.audioLength
        Glide.with(this@DetailActivity)
            .load(detail.data?.image)
            .into(binding.ivThread)
    }
}