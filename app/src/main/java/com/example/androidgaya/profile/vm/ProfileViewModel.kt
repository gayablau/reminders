package com.example.androidgaya.profile.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    fun editUsername(oldUsername: String, newUsername: String) {
        remindersRepo.editUsername(oldUsername, newUsername)
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }

    fun setUsername(username : String) {
        userRepo.setUsername(getApplication(), username)
    }
}