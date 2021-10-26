package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.interfaces.LoggedInUserInterface
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.socket.SocketDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LoggedInUserRepo(context: Context) : LoggedInUserInterface {

    companion object {
        val EMPTY = ""
    }

    @Inject
    lateinit var loggedInUserDao: LoggedInUserDao

    @Inject
    lateinit var socketDao: SocketDao


    init {
        (context as AppDataGetter).getAppComponent()?.injectLoggedInUserRepo(this)
    }

    override fun getLoggedInUsername(context: Context): String {
        return loggedInUserDao.getLoggedInUsername() ?: EMPTY
    }

    override fun getLoggedInUserId(context: Context): String {
        return loggedInUserDao.getLoggedInId() ?: EMPTY
    }


    override fun setLoggedIn(id: String, username: String): Unit = runBlocking {
        launch {
            loggedInUserDao.deleteOldLogins()
            loggedInUserDao.addLoggedInUser(LoggedInUserEntity(id, username))
        }
    }

    override fun logoutFromDB(): Unit = runBlocking {
        launch {
            loggedInUserDao.deleteOldLogins()
        }
    }

    override fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserDao.getLoggedInUserFromDBLive()
    }


    override fun login(context: Context, username: String, password: String) {
        socketDao.listenOnce(context.getString(R.string.connect_user), context.getString(R.string.user_id), connectUser, username, password)
    }

    override fun changeUsername(context: Context, callback: (callbackData : Array<Any>, userDetails: List<Any>) -> Unit, oldUsername: String, newUsername: String) {
        socketDao.listenOnce(context.getString(R.string.change_username_if_able), context.getString(R.string.change_username), callback, oldUsername, newUsername)
    }


    private val connectUser: (Array<Any>, List<Any>) -> Unit = { dataFromSocket: Array<Any>, dataFromClient: List<Any> ->
        if (dataFromSocket[0].toString().isNotBlank()) {
            setLoggedIn(dataFromSocket[0].toString(), dataFromClient[0].toString())
        }
    }
}