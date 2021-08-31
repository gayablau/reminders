package com.example.androidgaya.repositories.types

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class UserJson(
        @Json(name = "sharedId") val sharedId : Int = -1,
        @Json(name = "username") val username : String = "",
        @Json(name = "password") val password : String = "",
)