package com.example.androidgaya.main.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class MainViewModel : ViewModel() {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
        UserRepo.setUsername(username)
    }

    fun setIsLoggedIn(isLoggedIn : Boolean) {
        UserRepo.setIsLoggedIn(isLoggedIn)
    }

    fun setUsername(username : String) {
        UserRepo.setUsername(username)
    }

    fun getUsername() : String {
        return UserRepo.getUsername()
    }
}