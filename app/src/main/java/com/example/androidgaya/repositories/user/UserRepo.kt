package com.example.androidgaya.repositories.user

import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import com.example.androidgaya.R
import com.example.androidgaya.repositories.Reminder
import com.example.androidgaya.repositories.implementetions.SharedPref
import com.example.androidgaya.repositories.reminder.RemindersRepo
import java.security.AccessController.getContext
import java.util.*

class UserRepo {
    private var username: String = ""
    private var isLoggedIn: Boolean = false
    //val prefs : SharedPreferences = getContext().getSharedPreferences(getString(R.string.user_details_sp), context.getPackageName().MODE_PRIVATE);


    fun isUserLoggedIn() : Boolean {
        //return SharedPref.read(getString(R.id.us))
        return isLoggedIn
    }

    fun getUsername() : String {
        return username
    }

    fun setIsLoggedIn(isLoggedIn : Boolean) {
        this.isLoggedIn = isLoggedIn
    }

    fun setUsername(username : String) {
        this.username = username
    }
}