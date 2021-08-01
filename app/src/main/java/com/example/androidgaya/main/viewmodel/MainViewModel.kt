package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)


    fun setUsername(username : String) {
        userRepo.setUsername(getApplication(), username)
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
    }
}