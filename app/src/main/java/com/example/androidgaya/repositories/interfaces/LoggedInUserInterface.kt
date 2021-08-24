package com.example.androidgaya.repositories.interfaces
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity

interface LoggedInUserInterface {
    fun isUserLoggedIn(context: Context): Boolean
    fun getUsername(context: Context): String?
    fun setUsername(context: Context, username: String)
}