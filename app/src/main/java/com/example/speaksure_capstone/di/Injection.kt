package com.example.speaksure_capstone.di

import com.example.speaksure_capstone.data.CommentRepository
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.network.ApiConfig
import okhttp3.RequestBody

object Injection {
    fun provideRepository(query: String,token: String): ThreadRepository {
        val apiService = ApiConfig.getApiService()
        return ThreadRepository(apiService,token,query)
    }

    fun provideCommentRepository(threadId: Int,token: String): CommentRepository {
        val apiService = ApiConfig.getApiService()
        return CommentRepository(apiService,token,threadId)
    }
}