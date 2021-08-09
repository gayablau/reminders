package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo : RemindersRepo = RemindersRepo(application)

    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getMyRemindersIds(username: String) : List<Int> {
        return remindersRepo.getMyRemindersIds(username)
    }

    fun getRemindersByUsernameList(username: String) : List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(username)
    }
}