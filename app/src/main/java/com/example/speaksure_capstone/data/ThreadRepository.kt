package com.example.speaksure_capstone.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.speaksure_capstone.network.ApiService
import com.example.speaksure_capstone.database.ThreadDatabase
import com.example.speaksure_capstone.response.ListThreads

class ThreadRepository(private val threadDatabase: ThreadDatabase,private val query : String, private val apiService: ApiService, private val token: String) {
    fun getThread(): LiveData<PagingData<ListThreads>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = ThreadRemoteMediator(threadDatabase,query,apiService,token),
            pagingSourceFactory = {
                threadDatabase.threadDao().getAllThread()
            }
        ).liveData
    }
}