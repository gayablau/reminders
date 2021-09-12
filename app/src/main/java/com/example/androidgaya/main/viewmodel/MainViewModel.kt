package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class MainViewModel(application: Application,
                    private val socketRepo: SocketRepo) : AndroidViewModel(application) {

    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    var username: String
    var userId: Int

    init {
        updateLoggedInUser()
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
        getAllReminders(userId)
    }

    fun logout() {
        loggedInUserRepo.logout(getApplication())
        socketRepo.logout()
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

    private fun getAllReminders(userId: Int) {
        socketRepo.getAllReminders(userId)
    }
}
