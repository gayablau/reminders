package com.example.androidgaya.repositories.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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

    @Inject
    lateinit var application: Context

    private val userRepoCoroutineJob = SupervisorJob()
    private val userRepoCoroutineScope = CoroutineScope(Dispatchers.IO + userRepoCoroutineJob)

    private var loggedInUserPref: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.user_details_sp), MODE_PRIVATE)


    init {
        (context as ReminderApplication).getAppComponent()?.injectLoggedInUserRepo(this)
    }

    override fun getLoggedInUsername(context: Context): String {
        return loggedInUserDao.getLoggedInUsername(getLoggedInUserId(context)) ?: EMPTY
    }

    override fun getLoggedInUserId(context: Context): String {
        return loggedInUserPref.getString(context.getString(R.string.user_id), EMPTY) ?: EMPTY
    }

    override suspend fun setLoggedIn(context: Context, userId: String, username: String) {
        loggedInUserPref.edit().putString(context.getString(R.string.user_id), userId)?.apply()
        loggedInUserDao.addLoggedInUser(LoggedInUserEntity(userId, username))
    }

    override suspend fun updateLoggedIn(userId: String, username: String) {
        loggedInUserDao.updateLoggedInUser(LoggedInUserEntity(userId, username))
    }

    override suspend fun logout(context: Context) {
        loggedInUserPref.edit().putString(context.getString(R.string.user_id), EMPTY)?.apply()
        socketDao.emit(context.getString(R.string.logout))
    }

    override fun getLoggedInUserFromDB(userId: String): LiveData<List<String>> {
        return loggedInUserDao.getLoggedInUserFromDBLive(userId)
    }

    override suspend fun login(context: Context,
                               userPayload: UserPayload,
                               callback: (callbackData: Array<Any>,
                                          dataFromClient: String) -> Unit) {
        socketDao.listenOnce(context.getString(R.string.connect_user),
                context.getString(R.string.user_id),
                callback,
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
}