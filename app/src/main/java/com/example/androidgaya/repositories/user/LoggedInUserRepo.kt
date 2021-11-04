package com.example.androidgaya.repositories.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.interfaces.LoggedInUserInterface
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.UserPayload
import com.example.androidgaya.repositories.socket.SocketDao
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoggedInUserRepo(context: Context) : LoggedInUserInterface {

    companion object {
        val EMPTY = ""
    }

    @Inject
    lateinit var loggedInUserDao: LoggedInUserDao

    @Inject
    lateinit var socketDao: SocketDao

    @Inject
    lateinit var userPayloadAdapter: JsonAdapter<UserPayload>

    private val userRepoCoroutineJob = SupervisorJob()
    private val userRepoCoroutineScope = CoroutineScope(Dispatchers.IO + userRepoCoroutineJob)

    init {
        (context as ReminderApplication).getAppComponent()?.injectLoggedInUserRepo(this)
    }

    override fun getLoggedInUsername(context: Context): String {
        return loggedInUserDao.getLoggedInUsername() ?: EMPTY
    }

    override fun getLoggedInUserId(context: Context): String {
        return loggedInUserDao.getLoggedInId() ?: EMPTY
    }

    override suspend fun setLoggedIn(id: String, username: String) {
        loggedInUserDao.deleteOldLogins()
        loggedInUserDao.addLoggedInUser(LoggedInUserEntity(id, username))
    }

    override suspend fun updateLoggedIn(id: String, username: String) {
        loggedInUserDao.updateLoggedInUser(LoggedInUserEntity(id, username))
    }

    override suspend fun logoutFromDB() {
        loggedInUserDao.deleteOldLogins()
    }

    override suspend fun logout(context: Context) {
        logoutFromDB()
        socketDao.emit(context.getString(R.string.logout))
    }

    override fun getLoggedInUserFromDB(): LiveData<List<LoggedInUserEntity>> {
        return loggedInUserDao.getLoggedInUserFromDBLive()
    }

    override suspend fun login(context: Context, userPayload: UserPayload) {
        socketDao.listenOnce(context.getString(R.string.connect_user),
                context.getString(R.string.user_id),
                ::connectUser,
                userPayloadAdapter.toJson(userPayload))
    }

    override suspend fun changeUsername(context: Context,
                                callback: (callbackData: Array<Any>,
                                           dataFromClient: String) -> Unit,
                                newUsername: String) {
        socketDao.listenOnce(context.getString(R.string.change_username_if_able),
                context.getString(R.string.change_username),
                callback,
                newUsername)
    }

    private fun connectUser(dataFromSocket: Array<Any>, dataFromClient: String) {
        userRepoCoroutineScope.launch {
            if (dataFromSocket[0].toString().isNotBlank()) {
                runCatching {
                    userPayloadAdapter.fromJson(dataFromClient)?.let {
                        setLoggedIn(dataFromSocket[0].toString(), it.username) }
                }
            }
        }
    }
}