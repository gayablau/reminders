package com.example.androidgaya.repositories.interfaces
import android.content.Context

interface UserInterface {
    fun isUserLoggedIn(context: Context): Boolean
    fun getUsername(context: Context): String?
    fun setUsername(context: Context, username: String)
    fun areDetailsOK(username: String, password: String) : Boolean
    fun isUserExists(username: String) : Boolean
    fun createUser(username: String, password: String)
}