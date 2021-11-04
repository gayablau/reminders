package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.UserPayload
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo
    var username: String
    var userId: String

    init {
        (application as ReminderApplication).getAppComponent()?.injectLogin(this)
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun connectUser(userPayload: UserPayload, callback: (callbackData: Array<Any>, userDetails: String) -> Unit) {
        viewModelScope.launch {
            loggedInUserRepo.login(getApplication(), userPayload, callback)
        }
    }

    suspend fun setLoggedIn(application: Application, userId: String, username: String) {
        loggedInUserRepo.setLoggedIn(application, userId, username)
    }
}