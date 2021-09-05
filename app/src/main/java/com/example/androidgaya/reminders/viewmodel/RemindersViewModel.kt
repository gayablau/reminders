package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class RemindersViewModel(application: Application, val socketRepo : SocketRepo) : AndroidViewModel(application) {

    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    private var userRepo: UserRepo = UserRepo(application)
    private var username: String = ""
    private var userId: Int = 0
    lateinit var remindersList: LiveData<List<ReminderEntity>?>

    init {
        getMyReminders()
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersRepo.deleteReminder(reminderEntity)
        socketRepo.deleteReminder(reminderEntity)
    }

    private fun getRemindersByUserId(): LiveData<List<ReminderEntity>?> {
        username = getUsername()
        userId = userRepo.findUserIdByUsername(username)
        return remindersRepo.getReminders(userId)
    }

    fun getUsername(): String {
        return loggedInUserRepo.getLoggedInUsername(getApplication())
    }

    fun getMyReminders() {
        remindersList = getRemindersByUserId()
    }
}