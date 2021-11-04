package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.notifications.NotificationUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo

    @Inject
    lateinit var remindersRepo: RemindersRepo

    var username: String
    val userId: String

    init {
        (application as ReminderApplication).getAppComponent()?.injectMain(this)
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun logout() {
        viewModelScope.launch {
            loggedInUserRepo.logout(getApplication())
            NotificationUtils.cancelAll(getApplication(), remindersRepo.getMyRemindersIds(userId))
        }
    }

    fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }
}
