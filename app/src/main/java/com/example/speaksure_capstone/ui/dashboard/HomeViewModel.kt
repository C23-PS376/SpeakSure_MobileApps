package com.example.speaksure_capstone.ui.dashboard

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.di.Injection
import com.example.speaksure_capstone.response.ListThreads

class HomeViewModel(private val threadRepository: ThreadRepository): ViewModel() {

    fun getThread(): LiveData<PagingData<ListThreads>> {
        return threadRepository.getThread().cachedIn(viewModelScope)
    }

    fun searchThreads(query: String): LiveData<PagingData<ListThreads>> {
        return threadRepository.searchThreads(query).cachedIn(viewModelScope)
    }

    class HomeViewModelFactory(private val query: String,private val token: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(Injection.provideRepository(query,token)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}