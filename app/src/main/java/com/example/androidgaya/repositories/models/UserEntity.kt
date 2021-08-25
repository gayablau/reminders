package com.example.androidgaya.repositories.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
class UserEntity(@ColumnInfo(name = "sharedId") var sharedId: Int, @ColumnInfo(name = "username") var username: String, @ColumnInfo(name = "password") var password: String) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
}
