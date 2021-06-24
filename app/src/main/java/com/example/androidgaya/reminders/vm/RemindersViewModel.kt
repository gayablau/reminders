package com.example.androidgaya.reminders.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo
import java.util.*

class RemindersViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    fun getRemindersMap() : Map<String, ArrayList<Reminder>> {
        return remindersRepo.remindersMap
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }
}