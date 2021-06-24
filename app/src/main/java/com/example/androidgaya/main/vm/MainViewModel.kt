package com.example.androidgaya.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
        userRepo.setUsername(getApplication(), username)
    }

    fun setIsLoggedIn(isLoggedIn : Boolean) {
        userRepo.setIsLoggedIn(getApplication(), isLoggedIn)
    }

    fun setUsername(username : String) {
        userRepo.setUsername(getApplication(), username)
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }
}