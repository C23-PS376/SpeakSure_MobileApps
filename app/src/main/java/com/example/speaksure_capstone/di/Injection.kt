package com.example.speaksure_capstone.di

import android.content.Context
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.database.ThreadDatabase
import com.example.speaksure_capstone.network.ApiConfig

object Injection {
    fun provideRepository(query: String,token: String, context: Context): ThreadRepository {
        val database= ThreadDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return ThreadRepository(database,query,apiService,token)
    }
}