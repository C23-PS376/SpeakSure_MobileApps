package com.example.speaksure_capstone.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.di.Injection
import com.example.speaksure_capstone.response.ListThreads

class HomeViewModel(threadRepository: ThreadRepository): ViewModel() {

    val thread: LiveData<PagingData<ListThreads>> =
        threadRepository.getThread().cachedIn(viewModelScope)


    class HomeViewModelFactory(private val token: String, private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(Injection.provideRepository(token,context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}