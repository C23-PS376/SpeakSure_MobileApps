package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("data")
	val data: DataDetail? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class UserDetail(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class DataDetail(

	@field:SerializedName("likes_count")
	val likesCount: String,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("comments_count")
	val commentsCount: String? = null,

	@field:SerializedName("audio_length")
	val audioLength: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("audio")
	val audio: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("user")
	val user: UserDetail? = null
)
