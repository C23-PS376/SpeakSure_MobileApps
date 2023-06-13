package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class CommentListResponse(

	@field:SerializedName("data")
	val dataComment: List<CommentItem>,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class CommentUser(

	@field:SerializedName("name")
	val name: String
)

data class CommentItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("audio_length")
	val audioLength: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("audio")
	val audio: Any,

	@field:SerializedName("commentUser")
	val commentUser: CommentUser
)
