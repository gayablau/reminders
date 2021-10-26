package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity

interface LoggedInUserInterface {
    fun getLoggedInUsername(context: Context): String?
    fun setLoggedIn(id: String, username: String): Unit
    fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?>
    fun getLoggedInUserId(context: Context): String?
    fun logoutFromDB()
    fun login(context: Context, username: String, password: String)
    fun changeUsername(context: Context, callback: (callbackData : Array<Any>, userDetails: List<Any>) -> Unit, oldUsername: String, newUsername: String)
}