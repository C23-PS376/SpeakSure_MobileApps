package com.example.speaksure_capstone.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.speaksure_capstone.network.ApiService
import com.example.speaksure_capstone.response.ListThreads

class ThreadRepository(private val apiService: ApiService, private val token: String, private val query: String) {
    fun getThread(): LiveData<PagingData<ListThreads>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = { ThreadPagingSource(apiService, token, query) }
        ).liveData
    }
    fun searchThreads(query: String): LiveData<PagingData<ListThreads>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = { ThreadPagingSource(apiService, token, query) }
        ).liveData
    }
}