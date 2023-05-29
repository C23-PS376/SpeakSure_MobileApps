package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class LoginRegisterResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataItem(

	@field:SerializedName("access_token")
	val accessToken: String? = null
)
