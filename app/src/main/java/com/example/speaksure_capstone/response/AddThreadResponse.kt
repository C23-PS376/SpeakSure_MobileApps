package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class AddThreadResponse(

	@field:SerializedName("data")
	val data: List<ThreadItem>,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("message")
	val message: String?,

	@SerializedName("error")
	val error: String?
)

data class ThreadItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("topic")
	val topic: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("audio")
	val audio: String,

	@field:SerializedName("title")
	val title: String,

	@SerializedName("audio_length")
	val audioLength: Float
)
