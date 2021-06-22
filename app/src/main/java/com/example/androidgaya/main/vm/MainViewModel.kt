package com.example.androidgaya.main.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepository

class MainViewModel : ViewModel() {
    private var repository : RemindersRepository = RemindersRepository.getInstance()

    fun addUsername(username: String) {
        repository.addUsername(username)
    }
}