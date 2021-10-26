package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo

class LoginViewModel(application: Application,
                     private val socketDao: SocketDao) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())

    init {
        updateLoggedInUser()
    }

    fun connectUser(username: String, password: String) {
        loggedInUserRepo.login(getApplication(), username, password)
    }

    private fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    private fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }
}