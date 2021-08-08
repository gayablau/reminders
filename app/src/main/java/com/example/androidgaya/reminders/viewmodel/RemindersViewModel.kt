package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var username: String? = null
    lateinit var remindersList: LiveData<List<ReminderEntity>>

    init {
        (application as AppDataGetter).getAppComponent()?.injectReminders(this)
        username = getUsername()
        getMyReminders(username)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersRepo.deleteReminder(reminderEntity)
    }

    fun getRemindersByUsername(username: String) : LiveData<List<ReminderEntity>> {
        return remindersRepo.getRemindersByUsername(username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getMyReminders(username: String?): LiveData<List<ReminderEntity>>? {
        if (username != null) {
            remindersList =  getRemindersByUsername(username)
        }
        return null
    }
}