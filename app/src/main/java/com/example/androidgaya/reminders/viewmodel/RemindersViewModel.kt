package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var remindersRepo: RemindersRepo

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo

    val userId: String

    init {
        (application as ReminderApplication).getAppComponent()?.injectReminders(this)
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
        getAllReminders()
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.deleteReminder(getApplication(), reminderEntity)
        }
    }

    private fun getAllReminders() {
        remindersRepo.getAllReminders(getApplication(), userId)
    }

    fun getRemindersByUserId(): LiveData<List<ReminderEntity>> {
        return remindersRepo.getRemindersFromDB(userId)
    }

    fun getLoggedInUser(): LiveData<List<String>> {
        return loggedInUserRepo.getLoggedInUserFromDB(userId)
    }
}