package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var remindersRepo: RemindersRepo

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo

    val userId: String

    init {
        (application as ReminderApplication).getAppComponent()?.injectDetails(this)
        userId = loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun addReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.createReminder(getApplication(), reminderEntity)
        }
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch {
            remindersRepo.editReminder(getApplication(), reminderEntity)
        }
    }

    fun getReminderByID(id: Int): ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}