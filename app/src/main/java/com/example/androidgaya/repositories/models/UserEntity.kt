package com.example.androidgaya.repositories.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String,
                 @ColumnInfo(name = "username") var username: String)
