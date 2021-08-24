package com.example.androidgaya.repositories.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity

@Dao
interface LoggedInUserDao {
    @Query("SELECT * FROM loggedIn")
    fun getLoggedInUserFromDB(): List<LoggedInUserEntity>?

    @Query("SELECT * FROM loggedIn")
    fun getLoggedInUserFromDBLive(): LiveData<List<LoggedInUserEntity>?>

    @Insert
    fun addLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Update
    fun updateLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Delete
    fun deleteLoggedInUser(loggedInUserEntity: LoggedInUserEntity)

    @Query("SELECT username FROM loggedIn")
    fun getLoggedInUsernameList(): List<String>?

    @Query("SELECT username FROM loggedIn LIMIT 1")
    fun getLoggedInUsername(): LiveData<String>?

    @Query("DELETE FROM loggedIn")
    fun deleteOldLogins()
}