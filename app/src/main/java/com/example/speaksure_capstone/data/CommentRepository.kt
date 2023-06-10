package com.example.speaksure_capstone.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.speaksure_capstone.network.ApiService
import com.example.speaksure_capstone.response.CommentItem
import okhttp3.RequestBody

class CommentRepository (private val apiService: ApiService, private val token: String, private val threadId : RequestBody){
    fun getComment(): LiveData<PagingData<CommentItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                CommentPagingSource(apiService,token,threadId)
            }
        ).liveData
    }
}