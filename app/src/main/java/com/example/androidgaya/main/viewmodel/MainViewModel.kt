package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    //lateinit var loggedInList : LiveData<List<LoggedInUserEntity>?>

    //var username : String = ""

/*    init {
        updateUsernameLive()
    }*/

/*    fun logout() {
        loggedInUserRepo.logout(username)
    }*/

    fun setUsername(username : String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun getUsernameStr() : String? {

        return loggedInUserRepo.getUsername(getApplication()).toString()
    }

    fun getMyRemindersIds(username: String) : List<Int> {
        return remindersRepo.getMyRemindersIds(username)
    }

    fun getRemindersByUsernameList(username: String) : List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(username)
    }

/*    fun updateUsernameLive() {
        loggedInList = loggedInUserRepo.loggedInList
        username = loggedInUserRepo.getUsernameStr() ?: ""
    }*/
}
