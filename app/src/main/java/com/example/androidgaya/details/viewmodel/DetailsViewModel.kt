package com.example.androidgaya.details.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class DetailsViewModel(application: Application, val socketRepo : SocketRepo) : AndroidViewModel(application) {
    private var remindersRepo: RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String = loggedInUserRepo.getLoggedInUsername(getApplication())
    var userId: Int = loggedInUserRepo.getLoggedInUserId(getApplication())

    fun addReminder(reminderEntity: ReminderEntity) {
        remindersRepo.addReminder(reminderEntity)
        socketRepo.createReminder(reminderEntity)
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        remindersRepo.editReminder(reminderEntity)
        socketRepo.editReminder(reminderEntity)
    }

    fun getReminderByID(id: Int): ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}