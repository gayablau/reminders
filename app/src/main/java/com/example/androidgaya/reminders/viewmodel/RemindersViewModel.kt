package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())
    lateinit var remindersList: LiveData<List<ReminderEntity>?>

    init {
        getAllReminders(userId)
        getMyReminders()
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.deleteReminder(getApplication(), reminderEntity)
        }
    }

    private fun getRemindersByUserId(): LiveData<List<ReminderEntity>?> {
        return remindersRepo.getRemindersFromDB(userId)
    }

    private fun getMyReminders() {
        remindersList = getRemindersByUserId()
    }

    private fun getAllReminders(userId: String) {
        viewModelScope.launch {
            remindersRepo.getAllReminders(getApplication(), userId)
        }
    }
}