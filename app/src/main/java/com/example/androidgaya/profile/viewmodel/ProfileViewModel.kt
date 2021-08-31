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

    private var userRepo : UserRepo = UserRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectProfile(this)
    }

    fun editUsername(oldUsername: String, newUsername: String) {
        userRepo.editUsername(oldUsername, newUsername)
        setUsername(newUsername)
        mSocket!!.emit((getApplication() as Context).getString(R.string.change_username),
                oldUsername,
                newUsername)
    }

    fun getUsername() : String {
        return loggedInUserRepo.getLoggedInUsername(getApplication())
    }

    fun setUsername(username : String) {
        loggedInUserRepo.setLoggedInUsername(getApplication(), username)
    }

    fun isUsernameExists(username: String) : Boolean {
       return userRepo.isUserExists(username)
    }
}