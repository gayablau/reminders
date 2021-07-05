package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.models.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo
import java.util.*

class RemindersViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    fun deleteReminderById(id : String, username : String) {
        return remindersRepo.deleteReminderById(id, username)
    }

    fun getRemindersByUsername(username : String) : ArrayList<Reminder>? {
        return remindersRepo.getRemindersByUsername(username)
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }


}