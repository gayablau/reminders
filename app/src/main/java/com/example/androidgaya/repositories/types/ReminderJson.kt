package com.example.androidgaya.repositories.types

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReminderJson(
        @Json(name = "id") val id: Int = -1,
        @Json(name = "header") val header: String = "",
        @Json(name = "description") val description: String = "",
        @Json(name = "user") val user: String = "",
        @Json(name = "time") val time: Long = 0,
        @Json(name = "createdAt") val createdAt: Long = 0,
)
