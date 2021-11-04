package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.LoggedInUserEntity
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

    fun connectUser(userPayload: UserPayload) {
        viewModelScope.launch {
            loggedInUserRepo.login(getApplication(), userPayload)
        }
    }

    fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }
}