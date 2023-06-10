package com.example.speaksure_capstone.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.speaksure_capstone.data.CommentRepository
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.CommentItem
import com.example.speaksure_capstone.response.CommentResponse
import com.example.speaksure_capstone.response.DetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(commentRepository: CommentRepository):ViewModel() {


    private val _detailThread = MutableLiveData<DetailResponse>()
    val detailThread: LiveData<DetailResponse> = _detailThread


    val comment : LiveData<PagingData<CommentItem>> = commentRepository.getComment().cachedIn(viewModelScope)




    fun getDetail(token: String, id: String) {
        val client = ApiConfig.getApiService().getDetail(token, id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    _detailThread.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setComment(
        token: String,
        threadId: RequestBody,
        comment: RequestBody,
        audioMultipart: MultipartBody.Part?
    ) {
        val client =
            ApiConfig.getApiService().setCommentThread(token, threadId, comment, audioMultipart)
        client.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, "sukses: ${response.message()}")
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    /*fun getComment(
        token: String,
        threadId: RequestBody,
    ){
        val client =
            ApiConfig.getApiService().getCommentThread(token, threadId)
        client.enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(
                call: Call<CommentListResponse>,
                response: Response<CommentListResponse>
            ) {
                if (response.isSuccessful) {
                    _setCommentThread.value = response.body()?.commentItem
                    Log.e(ContentValues.TAG, "onSucces: ${response.message()}")
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })

    }*/
}