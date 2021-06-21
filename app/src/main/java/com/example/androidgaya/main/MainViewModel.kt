package com.example.androidgaya.main

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepository

class MainViewModel constructor(val username: String) : ViewModel() {
    private var repository : RemindersRepository = RemindersRepository.getInstance()

    fun addUsername() {
        repository.addUsername(username)
    }
}