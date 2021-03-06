package com.example.androidgaya.repositories.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loggedIn")
data class LoggedInUserEntity(
        @PrimaryKey(autoGenerate = false) val userId: String,
        var username: String
)