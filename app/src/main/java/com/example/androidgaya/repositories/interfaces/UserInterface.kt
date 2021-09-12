package com.example.androidgaya.repositories.interfaces

import com.example.androidgaya.repositories.models.UserEntity

interface UserInterface {
    fun editUsername(oldUsername: String, newUsername: String)
    fun insertUser(userEntity: UserEntity)
    fun areDetailsOK(username: String, password: String): Boolean
    fun isUserExists(username: String): Boolean
    fun findUserIdByUsername(username: String): Int
}