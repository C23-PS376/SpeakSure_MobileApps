package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<ThreadData>?,

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("error")
    val error: String?
)

data class ThreadData(

    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("threadId")
    val threadId: String?,

    @field:SerializedName("text")
    val text: String?,

    @field:SerializedName("audio")
    val audio: String?
)
