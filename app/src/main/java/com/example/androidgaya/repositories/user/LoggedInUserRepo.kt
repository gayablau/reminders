package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.LoggedInUserDao
import com.example.androidgaya.repositories.interfaces.LoggedInUserInterface
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        return loggedInUserPref.getString(context.getString(R.string.username), EMPTY) != EMPTY
    }

    override fun getLoggedInUsername(context: Context): String? {
        return loggedInUserPref.getString(context.getString(R.string.username), EMPTY)
    }

    override fun getLoggedInUserId(context: Context): Int? {
        return loggedInUserPref.getInt(context.getString(R.string.UserId), 0)
    }

    override fun setLoggedInUsername(context: Context, username: String) {
        loggedInUserPref.edit().putString(context.getString(R.string.username), username).apply()
        loggedInUserDao.deleteOldLogins()
        loggedInUserDao.addLoggedInUser(LoggedInUserEntity(username))
    }

    override fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserDao.getLoggedInUserFromDBLive()
    }
}