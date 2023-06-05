package com.example.speaksure_capstone.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.DetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel:ViewModel() {

    private val _detailThread = MutableLiveData<DetailResponse>()
    val detailThread: LiveData<DetailResponse> = _detailThread

    fun getDetail(token: String, id : String) {
        val client = ApiConfig.getApiService().getDetail(token,id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>)
            {if (response.isSuccessful) {
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
}
