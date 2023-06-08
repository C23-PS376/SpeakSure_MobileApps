package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class LikeResponse(

	@field:SerializedName("data")
	val data: DataLike? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataLike(

	@field:SerializedName("thread_id")
	val threadId: Int? = null
)
