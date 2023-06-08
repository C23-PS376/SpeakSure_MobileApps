package com.example.speaksure_capstone.network

import com.example.speaksure_capstone.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("user/{user_id}")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("user_id") id: String
    ): Call<ProfileResponse>

    @GET("threads/{thread_id}")
    fun getDetail(
        @Header("Authorization") token: String,
        @Path("thread_id") id: String
    ): Call<DetailResponse>

    @Multipart
    @POST("threads")
    fun addNewThread(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("topic") topic: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part audio : MultipartBody.Part
    ): Call<AddThreadResponse>

    @POST("threads/{thread_id}/likes")
    fun like(
        @Header("Authorization") token: String,
        @Path("thread_id") id: String
    ):Call <LikeResponse>

    @DELETE("threads/{thread_id}/likes")
    fun unlike(
        @Header("Authorization") token: String,
        @Path("thread_id") id: String
    ):Call<String>

    @GET("threads")
    suspend fun searchThread(
        @Header("Authorization") token: String,
        @Query("keyword") keyword:String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ):ListThreadResponse

    @Multipart
    @POST("threads/{threadId}/comment")
    fun setCommentThread(
        @Header("Authorization") token: String,
        @Path("threadId") threadId: RequestBody,
        @Part("text") text: RequestBody,
        @Part audio : MultipartBody.Part
    ): Call<CommentResponse>
}