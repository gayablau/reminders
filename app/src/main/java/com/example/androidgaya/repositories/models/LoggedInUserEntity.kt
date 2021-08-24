package com.example.androidgaya.repositories.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loggedIn")
class LoggedInUserEntity(@ColumnInfo(name = "username") var username: String) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
}