package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.UserPayload

interface LoggedInUserInterface {
    fun getLoggedInUsername(context: Context): String
    fun getLoggedInUserId(context: Context): String
    fun getLoggedInUserFromDB(userId: String): LiveData<List<String>>
    suspend fun logout(context: Context)
    suspend fun login(context: Context,
                      userPayload: UserPayload,
                      callback: (callbackData: Array<Any>,
                                 dataFromClient: String) -> Unit)
    suspend fun updateLoggedIn(id: String, username: String)
    suspend fun setLoggedIn(context: Context, id: String, username: String)
    suspend fun changeUsername(context: Context,
                               callback: (callbackData: Array<Any>,
                                          dataFromClient: String) -> Unit,
                               newUsername: String)
}