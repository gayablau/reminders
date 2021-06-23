package com.example.androidgaya.reminders.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo
import java.util.*

class RemindersViewModel : ViewModel() {
    private var repo : RemindersRepo = RemindersRepo.getInstance()

    fun getRemindersMap() : Map<String, ArrayList<Reminder>> {
        return repo.getRemindersMap()
    }

    fun getUsername() : String {
        return UserRepo.getUsername()
    }
}