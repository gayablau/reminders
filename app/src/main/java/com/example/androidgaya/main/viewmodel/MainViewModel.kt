package com.example.androidgaya.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
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

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectMain(this)
        updateLoggedInUser()
        getAllReminders()
    }

    fun logout() {
        loggedInUserRepo.logout(getApplication())
        mSocket!!.emit((getApplication() as Context).getString(R.string.logout))
    }

    fun getUsernameStr() : String {
        return loggedInUserRepo.getLoggedInUsername(getApplication())
    }

    fun getMyRemindersIds(userId : Int) : List<Int> {
        return remindersRepo.getMyRemindersIds(userId)
    }

    fun getRemindersByUserIdList(userId : Int) : List<ReminderEntity> {
        return remindersRepo.getRemindersByUsernameList(userId)
    }

    fun getLoggedInUser() : LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }

    fun getAllReminders() {
        mSocket!!.emit((getApplication() as Context).getString(R.string.get_all_reminders))
    }

    fun getUserId(username : String) : Int {
        return userRepo.findUserIdByUsername(username)
    }
}
