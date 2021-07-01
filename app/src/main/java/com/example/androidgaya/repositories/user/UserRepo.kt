package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.androidgaya.R

class UserRepo(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.user_details_sp), MODE_PRIVATE)

    fun isUserLoggedIn(context: Context): Boolean {
        return prefs.getString(context.getString(R.string.username), "") != ""
    }

    fun getUsername(context: Context): String? {
        return prefs.getString(context.getString(R.string.username), "")
    }

    fun setUsername(context: Context, username: String) {
        prefs.edit().putString(context.getString(R.string.username), username)?.apply()
    }
}