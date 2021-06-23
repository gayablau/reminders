package com.example.androidgaya.profile.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class ProfileViewModel : ViewModel() {
    private var repo : RemindersRepo = RemindersRepo.getInstance()

    fun editUsername(oldUsername: String, newUsername: String) {
        repo.editUsername(oldUsername, newUsername)
    }

    fun getUsername() : String {
        return UserRepo.getUsername()
    }

    fun setUsername(username : String) {
        UserRepo.setUsername(username)
    }
}