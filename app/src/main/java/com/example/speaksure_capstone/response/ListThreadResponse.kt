package com.example.speaksure_capstone.response

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ListThreadResponse(

	@field:SerializedName("data")
	val data: List<ListThreads>,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class Topic(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ListThreads(

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
	val createdAt: String,

	@field:SerializedName("topic")
	val topic: Topic? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("audio")
	val audio: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

class UserConverter {

	@TypeConverter
	fun fromUser(user: User?): String? {
		return Gson().toJson(user)
	}

	@TypeConverter
	fun toUser(userString: String?): User? {
		return Gson().fromJson(userString, User::class.java)
	}
}

class TopicConverter {

	@TypeConverter
	fun fromTopic(topic: Topic?): String? {
		return Gson().toJson(topic)
	}

	@TypeConverter
	fun toTopic(topicString: String?): Topic? {
		return Gson().fromJson(topicString, Topic::class.java)
	}
}
