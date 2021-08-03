package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.login.ui.AppDataGetter
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var userDao: UserDao
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectProfile(this)
    }

    fun editUsername(oldUsername: String, newUsername: String) {
        remindersRepo.editUsername(oldUsername, newUsername)
        userDao.editUsername(oldUsername, newUsername)
        //userRepo.editUsername(oldUsername, newUsername)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun isUsernameExists(username: String) : Boolean {
        return remindersRepo.isUsernameExists(username)
    }
}