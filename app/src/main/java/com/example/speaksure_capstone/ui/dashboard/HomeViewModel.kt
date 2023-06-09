package com.example.speaksure_capstone.ui.dashboard

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.di.Injection
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.ListThreadResponse
import com.example.speaksure_capstone.response.ListThreads
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val threadRepository: ThreadRepository): ViewModel() {

    fun getThread(): LiveData<PagingData<ListThreads>> {
        return threadRepository.getThread().cachedIn(viewModelScope)
    }

    fun searchThreads(query: String): LiveData<PagingData<ListThreads>> {
        return threadRepository.searchThreads(query).cachedIn(viewModelScope)
    }

    class HomeViewModelFactory(private val query: String,private val token: String, private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(Injection.provideRepository(query,token,context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}