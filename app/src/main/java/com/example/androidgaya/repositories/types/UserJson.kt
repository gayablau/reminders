package com.example.androidgaya.repositories.types

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class UserJson(
        @Json(name = "userId") val userId: String = "",
        @Json(name = "username") val username: String = "",
)
