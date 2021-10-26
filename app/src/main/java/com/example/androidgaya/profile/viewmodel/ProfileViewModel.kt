package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo

class ProfileViewModel(application: Application,
                       private val socketDao: SocketDao) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun editUsername(newUsername: String, callback: (callbackData : Array<Any>, userDetails: List<Any>) -> Unit) {
        loggedInUserRepo.changeUsername(getApplication(), callback, username, newUsername)
    }

    fun setLoggedIn(newUsername: String) {
        loggedInUserRepo.setLoggedIn(userId, newUsername)
    }
}