package com.example.androidgaya.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import io.socket.client.Socket
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    lateinit var loggedInUserList : LiveData<List<LoggedInUserEntity>?>

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectMain(this)
        updateLoggedInUser()
        getAllReminders()
    }


/*    fun logout() {
        loggedInUserRepo.logout(username)
    }*/

    fun setUsername(username : String) {
        loggedInUserRepo.setLoggedInUsername(getApplication(), username)
    }

    fun getUsernameStr() : String? {
        return loggedInUserRepo.getLoggedInUsername(getApplication()).toString()
    }

    fun getMyRemindersIds(username: String) : List<Int> {
        return remindersRepo.getMyRemindersIds(username)
    }

    fun getRemindersByUsernameList(username: String) : List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(username)
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
