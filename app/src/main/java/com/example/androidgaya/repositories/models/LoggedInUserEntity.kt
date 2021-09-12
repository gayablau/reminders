package com.example.androidgaya.repositories.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loggedIn")
class LoggedInUserEntity(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: Int,
                         @ColumnInfo(name = "username") var username: String) {
}