package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class DetailsViewModel(application: Application,
                       private val socketDao: SocketDao) : AndroidViewModel(application) {
    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: String = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun addReminder(reminderEntity: ReminderEntity) {
        remindersRepo.createReminder(getApplication(), reminderEntity)
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        remindersRepo.editReminder(getApplication(), reminderEntity)
    }

    fun getReminderByID(id: Int): ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}