package com.example.androidgaya.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var remindersDao: RemindersDao
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectProfile(this)
    }

    fun editUsername(oldUsername: String, newUsername: String) {
        remindersDao.editUsername(oldUsername, newUsername)
        userDao.editUsername(oldUsername, newUsername)
        loggedInUserRepo.setUsername(getApplication(), newUsername)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun isUsernameExists(username: String) : Boolean {
        if (userDao.findUserByUsername(username) == null) {return false}
        return true
    }
}