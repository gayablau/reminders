package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity

interface LoggedInUserInterface {
    fun getLoggedInUsername(context: Context): String?
    fun setLoggedIn(id: String, username: String)
    fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?>
    fun getLoggedInUserId(context: Context): String?
    fun logoutFromDB()
    fun logout(context: Context)
    fun login(context: Context, username: String, password: String)
    suspend fun updateLoggedIn(id: String, username: String)
    fun changeUsername(context: Context,
                       callback: (callbackData: Array<Any>,
                                  userDetails: List<Any>) -> Unit,
                       newUsername: String)
}