package com.example.speaksure_capstone.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

/*data class DataItem(

	@field:SerializedName("access_token")
	val accessToken: String
)*/
