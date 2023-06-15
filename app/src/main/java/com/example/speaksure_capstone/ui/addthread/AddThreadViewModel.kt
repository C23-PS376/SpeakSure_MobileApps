package com.example.speaksure_capstone.ui.addthread

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.AddThreadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddThreadViewModel:ViewModel() {

    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    private val _toxicStatus = MutableLiveData<Boolean>()
    val toxicStatus: LiveData<Boolean> = _toxicStatus


    fun addThread(
        token:String,
        title: RequestBody,
        description: RequestBody,
        topic:RequestBody,
        imageMultipart: MultipartBody.Part?,
        audioMultipart: MultipartBody.Part?){
        val addNewThread = ApiConfig.getApiService().addNewThread(token,title, description,topic, imageMultipart,audioMultipart)
        Log.e("addThread","sampe sini")
        addNewThread.enqueue(object : Callback<AddThreadResponse> {
            override fun onResponse(
                call: Call<AddThreadResponse>,
                response: Response<AddThreadResponse>
            ){
                if(response.isSuccessful){
                    if(response.code() == 400){
                        Log.e("toxic", response.message())
                        _toxicStatus.value = true
                    }else{
                        _uploadStatus.value = false

                        val responseBody = response.body()
                        Log.e("success","$responseBody")
                        Log.e("success","${responseBody?.statusCode}")
                    }

                }else{
                    if(response.code() == 400){
                        Log.e("toxic", response.message())
                        _toxicStatus.value = true

                    }else{
                        _uploadStatus.value = true
                        Log.e("failed 1", "Error ${response.code()}: ${response.errorBody()} ")
                        Log.e("failed 2", "Error ${response.body()?.message}")
                        Log.e("failed 3", "Error ${response.body()?.error}")
                        Log.e("failed 4", "Error ${response.body()}")
                    }
                }
            }

            override fun onFailure(call: Call<AddThreadResponse>, t: Throwable) {
                _uploadStatus.value = true




            }
        })

    }
}

