package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)

    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }
}