package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import android.graphics.ColorSpace.Model
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject


class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var userRepo : UserRepo = UserRepo(application)
    private var username: String = ""
    private var userId: Int = 0
    lateinit var remindersList: LiveData<List<ReminderEntity>?>

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectReminders(this)
        getMyReminders()
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersRepo.deleteReminder(reminderEntity)
        mSocket!!.emit("deleteReminder",
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    private fun getRemindersByUsername() : LiveData<List<ReminderEntity>?> {
        username = getUsername()
        userId = userRepo.findUserIdByUsername(username)
        Log.i("wtf 2", remindersRepo.getReminders(userId).value.toString())
        return remindersRepo.getReminders(userId)
    }

    fun getUsername() : String {
        return loggedInUserRepo.getLoggedInUsername(getApplication()) ?: ""
    }

    fun getMyReminders() {
        remindersList = getRemindersByUsername()
    }
}