package com.example.androidgaya.reminders.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepository

class RemindersViewModel : ViewModel() {
    private var repository : RemindersRepository = RemindersRepository.getInstance()

    fun getRemindersMap() {
        repository.getRemindersMap()
    }
}