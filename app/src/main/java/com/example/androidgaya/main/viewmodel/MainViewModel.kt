package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var userRepo : UserRepo = UserRepo(application)
    lateinit var loggedInUserList : LiveData<List<LoggedInUserEntity>?>
    var userId : Int = 0

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectMain(this)
        updateLoggedInUser()
        getAllReminders()
        userId = loggedInUserRepo.getLoggedInUsername(application)?.let { userRepo.findUserIdByUsername(it) }!!
    }


    fun logout() {
        loggedInUserRepo.setLoggedInUsername(getApplication(), "")
        mSocket!!.emit("logout")
    }

    fun setUsername(username : String) {
        loggedInUserRepo.setLoggedInUsername(getApplication(), username)
    }

    fun getUsernameStr() : String? {
        return loggedInUserRepo.getLoggedInUsername(getApplication()).toString()
    }

    fun getMyRemindersIds() : List<Int> {
        return remindersRepo.getMyRemindersIds(userId)
    }

    fun getRemindersByUsernameList() : List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(userId)
    }

    fun getLoggedInUser() : LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }

    fun getAllReminders() {
        mSocket!!.emit("getAllReminders")
    }
}
