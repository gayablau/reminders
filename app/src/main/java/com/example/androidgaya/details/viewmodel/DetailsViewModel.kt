package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.models.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    fun addReminder(reminder : Reminder, username: String) {
        remindersRepo.addReminder(reminder, username)
    }

    fun editReminder(reminder : Reminder, username: String) {
        remindersRepo.editReminder(reminder, username)
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }

    fun getReminderByID(id : String, username: String) : Reminder? {
        return remindersRepo.getReminderByID(id, username)
    }
}