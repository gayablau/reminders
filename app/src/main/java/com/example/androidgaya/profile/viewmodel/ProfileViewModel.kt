package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun editUsername(newUsername: String, callback: (callbackData : Array<Any>, userDetails: List<Any>) -> Unit) {
        viewModelScope.launch {
            loggedInUserRepo.changeUsername(getApplication(), callback, username, newUsername)
        }
    }

    fun updateLoggedIn(newUsername: String) {
        viewModelScope.launch {
            loggedInUserRepo.updateLoggedIn(userId, newUsername)
        }
    }
}