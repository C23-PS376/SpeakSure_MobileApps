package com.example.speaksure_capstone.ui.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.speaksure_capstone.data.ThreadRepository
import com.example.speaksure_capstone.di.Injection
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(threadRepository: ThreadRepository): ViewModel() {

    private val _detailUser = MutableLiveData<ProfileResponse>()
    val detailUser: LiveData<ProfileResponse> = _detailUser


    fun getUser(token: String, id : String) {
        val client = ApiConfig.getApiService().getProfile(token,id)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>)
            {if (response.isSuccessful) {
                _detailUser.value = response.body()
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    class ProfileViewModelFactory(private val query: String,private val token: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(Injection.provideRepository(query,token)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}