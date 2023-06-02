package com.example.speaksure_capstone.network

import com.example.speaksure_capstone.response.ListThreadResponse
import com.example.speaksure_capstone.response.LoginRegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Call<LoginRegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginRegisterResponse>


    @GET("threads")
    suspend fun getThread(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListThreadResponse

}