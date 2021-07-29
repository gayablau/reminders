package com.example.androidgaya.repositories.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.example.androidgaya.repositories.models.UserEntity

@Dao
interface RemindersDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getRecords(): List<UserEntity>

}