package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class Data(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("threads_count")
	val threadsCount: String? = null,

	@field:SerializedName("comments_count")
	val commentsCount: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("audio_length")
	val audioLength: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("audio")
	val audio: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Any? = null
)
