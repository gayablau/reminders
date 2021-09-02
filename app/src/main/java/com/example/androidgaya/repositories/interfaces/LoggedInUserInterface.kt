package com.example.androidgaya.repositories.interfaces
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity

interface LoggedInUserInterface {
    fun isUserLoggedIn(context: Context): Boolean
    fun getLoggedInUsername(context: Context): String?
    fun setLoggedIn(context: Context, id: Int, username: String)
    fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?>
    fun getLoggedInUserId(context: Context): Int
    fun logout(context: Context)
}