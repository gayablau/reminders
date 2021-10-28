package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun addReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.createReminder(getApplication(), reminderEntity)
        }
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.editReminder(getApplication(), reminderEntity)
        }
    }

    fun getReminderByID(id: Int): ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}