package com.example.androidgaya.profile.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepo: UserRepo = UserRepo(application)
    private var loggedInUserRepo: LoggedInUserRepo = LoggedInUserRepo(application)
    var username: String
    var userId: Int

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectProfile(this)
        username = loggedInUserRepo.getLoggedInUsername(getApplication())
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun editUsername(newUsername: String) {
        userRepo.editUsername(username, newUsername)
        setLoggedInUsername(newUsername)
        mSocket!!.emit((getApplication() as Context).getString(R.string.change_username),
                username,
                newUsername)
    }


    fun setLoggedInUsername(username: String) {
        loggedInUserRepo.setLoggedIn(getApplication(), userId, username)
    }

    fun isUsernameExists(username: String): Boolean {
        return userRepo.isUserExists(username)
    }
}