package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.models.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    fun addReminder(reminder : Reminder, username: String) {
        remindersRepo.addReminder(reminder, username)
    }

    fun editReminder(reminder : Reminder, username: String) {
        remindersRepo.editReminder(reminder, username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getReminderByID(id : Int, username: String) : Reminder? {
        return remindersRepo.getReminderByID(id, username)
    }
}