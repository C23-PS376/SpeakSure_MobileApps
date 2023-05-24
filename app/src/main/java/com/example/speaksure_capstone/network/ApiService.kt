package com.example.speaksure_capstone.network

import com.example.speaksure_capstone.response.LoginResponse
import com.example.speaksure_capstone.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

}