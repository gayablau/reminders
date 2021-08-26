package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.interfaces.LoggedInUserInterface
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import javax.inject.Inject

class LoggedInUserRepo(context: Context) : LoggedInUserInterface {

    companion object {
        val EMPTY = ""
    }

    @Inject
    lateinit var loggedInUserDao: LoggedInUserDao

    private var loggedInUserPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.user_details_sp), MODE_PRIVATE)

    init {
        (context as AppDataGetter).getAppComponent()?.injectLoggedInUserRepo(this)
    }

    override fun isUserLoggedIn(context: Context): Boolean {
        return loggedInUserPref.getString(context.getString(R.string.username_uppercase), EMPTY) != EMPTY
    }

    override fun getLoggedInUsername(context: Context): String {
        return loggedInUserPref.getString(context.getString(R.string.username_uppercase), EMPTY) ?: EMPTY
    }

    override fun getLoggedInUserId(context: Context): Int {
        return loggedInUserPref.getInt(context.getString(R.string.UserId), 0)
    }

    override fun setLoggedInUsername(context: Context, username: String) {
        loggedInUserPref.edit().putString(context.getString(R.string.username_uppercase), username).apply()
        loggedInUserDao.deleteOldLogins()
        loggedInUserDao.addLoggedInUser(LoggedInUserEntity(username))
    }

    override fun logout(context: Context) {
        loggedInUserPref.edit().putString(context.getString(R.string.username_uppercase), EMPTY).apply()
        loggedInUserDao.deleteOldLogins()
    }

    override fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserDao.getLoggedInUserFromDBLive()
    }
}