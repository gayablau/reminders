package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)


    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
    }
}