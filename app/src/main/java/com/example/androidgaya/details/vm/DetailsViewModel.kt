package com.example.androidgaya.details.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo

class DetailsViewModel : ViewModel() {
    private var repo : RemindersRepo = RemindersRepo.getInstance()

    fun addReminder(reminder : Reminder, username: String) {
        repo.addReminder(reminder, username)
    }

    fun editReminder(reminder : Reminder, username: String) {
        repo.addReminder(reminder, username)
    }
}