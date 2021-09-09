package com.example.androidgaya.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import java.util.*
import javax.inject.Inject

class LoginViewModel(application: Application, val socketRepo : SocketRepo) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: Int = loggedInUserRepo.getLoggedInUserId(getApplication())

    init {
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
        socketRepo.updateUsers()
        return userRepo.isUserExists(username)
    }

    fun createNewUser(username: String, password: String) {
        userId = Random(System.currentTimeMillis()).nextInt(10000)
        insertUser(UserEntity(userId, username, password))
        setUsername(userId, username)
        socketRepo.createUser(userId, username, password)
    }

    fun connectUser(username: String) {
        userId = userRepo.findUserIdByUsername(username)
        setUsername(userId, username)
        socketRepo.connectUser(userId, username)
    }

    fun getAllUsers() {
        socketRepo.getAllUsers()
    }

    fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }
}