package com.example.androidgaya.repositories.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgaya.repositories.models.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsersFromDB(): LiveData<List<UserEntity>?>

    @Insert
    fun insertUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE username LIKE :username")
    fun findUserByUsername(username: String): UserEntity?

    @Query("SELECT sharedId FROM users WHERE username LIKE :username")
    fun findUserIdByUsername(username: String): Int

    @Query("UPDATE users SET username = :newUsername WHERE username = :oldUsername")
    fun editUsername(oldUsername: String, newUsername: String)

    @Query("SELECT * FROM users WHERE username LIKE :username AND password LIKE :password")
    fun areDetailsOK(username: String, password: String) : UserEntity?
}