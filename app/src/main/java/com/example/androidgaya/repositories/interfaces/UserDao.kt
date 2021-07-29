package com.example.androidgaya.repositories.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgaya.repositories.models.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllRecordsFromDB(): List<UserEntity>?

    @Insert
    fun insertRecord(userEntity: UserEntity)
}