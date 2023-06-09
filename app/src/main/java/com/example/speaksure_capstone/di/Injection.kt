package com.example.speaksure_capstone.di

import android.content.Context
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.network.ApiConfig

object Injection {
    fun provideRepository(query: String,token: String, context: Context): ThreadRepository {
        val apiService = ApiConfig.getApiService()
        return ThreadRepository(apiService,token,query)
    }
}