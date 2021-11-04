package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo

    var username: String
    val userId: String

    init {
        (application as ReminderApplication).getAppComponent()?.injectProfile(this)
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun editUsername(newUsername: String, callback: (callbackData: Array<Any>, userDetails: String) -> Unit) {
        viewModelScope.launch {
            loggedInUserRepo.changeUsername(getApplication(), callback, newUsername)
        }
    }

    fun updateLoggedIn(newUsername: String) {
        viewModelScope.launch {
            loggedInUserRepo.updateLoggedIn(userId, newUsername)
        }
    }

    fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }
}