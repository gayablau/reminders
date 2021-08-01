package com.example.androidgaya.repositories.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(@ColumnInfo(name = "name")var username : String, @ColumnInfo(name = "password") var password: String) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
    //@ColumnInfo(name = "name") var username: String? = ""
    //@ColumnInfo(name = "password") var password: String? = ""
}