package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)


    fun setUsername(username : String) {
        userRepo.setUsername(getApplication(), username)
        remindersRepo.addUsername(username)
    }

    fun isUserLoggedIn() : Boolean {
        return userRepo.isUserLoggedIn(getApplication())
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }
}