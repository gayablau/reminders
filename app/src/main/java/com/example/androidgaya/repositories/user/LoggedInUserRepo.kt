package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.androidgaya.R
import com.example.androidgaya.repositories.interfaces.LoggedInUserInterface

class LoggedInUserRepo(context: Context) : LoggedInUserInterface {
    companion object {
        val EMPTY = ""
    }
    private var loggedInUserPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.user_details_sp), MODE_PRIVATE)

    override fun isUserLoggedIn(context: Context): Boolean {
        return loggedInUserPref.getString(context.getString(R.string.username), EMPTY) != EMPTY
    }

    override fun getUsername(context: Context): String? {
        return loggedInUserPref.getString(context.getString(R.string.username), EMPTY)
    }

    override fun setUsername(context: Context, username: String) {
        loggedInUserPref.edit().putString(context.getString(R.string.username), username).apply()
    }
}