package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils
import io.socket.client.Socket
import javax.inject.Inject

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var username: String = ""
    lateinit var remindersList: LiveData<List<ReminderEntity>>

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
                reminderEntity.username,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    private fun getRemindersByUsername() : LiveData<List<ReminderEntity>> {
        username = getUsername()
        //return remindersRepo.getReminders()
        return Transformations.map(remindersRepo.getReminders()) {
            it.filter { rem -> rem.username == username }
        }
    }

    fun getUsername() : String {
        return loggedInUserRepo.getLoggedInUsername(getApplication()) ?: ""
    }

    fun getMyReminders() {
        remindersList = getRemindersByUsername()
    }
}