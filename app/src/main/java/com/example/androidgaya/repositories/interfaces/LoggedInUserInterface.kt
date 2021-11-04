package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.UserPayload

interface LoggedInUserInterface {
    fun getLoggedInUsername(context: Context): String?
    suspend fun setLoggedIn(id: String, username: String)
    fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?>
    fun getLoggedInUserId(context: Context): String?
    suspend fun logoutFromDB()
    suspend fun logout(context: Context)
    suspend fun login(context: Context, userPayload: UserPayload)
    suspend fun updateLoggedIn(id: String, username: String)
    suspend fun changeUsername(context: Context,
                               callback: (callbackData: Array<Any>,
                                  dataFromClient: String) -> Unit,
                               newUsername: String)
}