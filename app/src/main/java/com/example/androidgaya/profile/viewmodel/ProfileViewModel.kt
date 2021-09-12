package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo

class ProfileViewModel(application: Application,
                       private val socketRepo: SocketRepo) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: Int = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun editUsername(newUsername: String) {
        userRepo.editUsername(username, newUsername)
        setLoggedInUsername(newUsername)
        socketRepo.changeUsername(username, newUsername)
    }

    fun setLoggedInUsername(username: String) {
        loggedInUserRepo.setLoggedIn(getApplication(), userId, username)
    }

    fun isUsernameExists(username: String): Boolean {
        return userRepo.isUserExists(username)
    }
}