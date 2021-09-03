package com.example.androidgaya.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import java.util.*
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String
    var userId: Int

    @Inject
    lateinit var mSocket: Socket

    init {
        (application as AppDataGetter).getAppComponent()?.injectLogin(this)
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
        updateLoggedInUser()
        getAllUsers()
    }

    fun insertUser(userEntity: UserEntity) {
        userRepo.insertUser(userEntity)
    }

    fun setUsername(userId: Int, username: String) {
        loggedInUserRepo.setLoggedIn(getApplication(), userId, username)
    }

    fun isUserLoggedIn(): Boolean {
        return loggedInUserRepo.isUserLoggedIn(getApplication())
    }

    fun areDetailsOK(username: String, password: String): Boolean {
        return userRepo.areDetailsOK(username, password)
    }

    fun isUserExists(username: String): Boolean {
        mSocket.emit((getApplication() as Context).getString(R.string.update_users))
        return userRepo.isUserExists(username)
    }

    fun createNewUser(username: String, password: String) {
        userId = Random(System.currentTimeMillis()).nextInt(10000)
        insertUser(UserEntity(userId, username, password))
        setUsername(userId, username)
        mSocket.emit((getApplication() as Context).getString(R.string.create_user), userId, username, password)
    }

    fun connectUser(username: String) {
        userId = userRepo.findUserIdByUsername(username)
        setUsername(userId, username)
        mSocket.emit((getApplication() as Context).getString(R.string.connect_user), userId, username)
    }

    fun getAllUsers() {
        mSocket.emit((getApplication() as Context).getString(R.string.get_all_users))
    }

    fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }
}