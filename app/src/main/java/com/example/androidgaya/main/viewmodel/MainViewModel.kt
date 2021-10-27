package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils

class MainViewModel(application: Application,
                    private val socketDao: SocketDao) : AndroidViewModel(application) {

    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    var username: String
    var userId: String

    init {
        updateLoggedInUser()
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun logout() {
        loggedInUserRepo.logout(getApplication())
        remindersRepo.logout(getApplication(), userId)
    }

    fun getMyRemindersIds(): List<Int> {
        return remindersRepo.getMyRemindersIds(userId)
    }

    fun getRemindersByUserIdList(): List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(userId)
    }

    private fun getLoggedInUser(): LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    private fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }
}
