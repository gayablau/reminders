package com.example.androidgaya.repositories.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidgaya.repositories.models.LoggedInUserEntity

@Dao
interface LoggedInUserDao {
    @Query("SELECT username FROM loggedIn WHERE userId LIKE :userId")
    fun getLoggedInUserFromDBLive(userId: String): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Update
    fun updateLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Delete
    fun deleteLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Query("SELECT username FROM loggedIn WHERE userId LIKE :userId")
    fun getLoggedInUsername(userId: String): String?

}