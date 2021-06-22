package com.example.androidgaya.profile.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepository

class ProfileViewModel : ViewModel() {
    private var repository : RemindersRepository = RemindersRepository.getInstance()

    fun editUsername(oldUsername: String, newUsername: String) {
        repository.editUsername(oldUsername, newUsername)
    }
}