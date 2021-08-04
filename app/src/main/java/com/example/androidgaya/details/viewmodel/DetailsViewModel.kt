package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import javax.inject.Inject

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectDetails(this)
    }

    fun addReminder(reminderEntity : ReminderEntity) {
        remindersRepo.addReminder(reminderEntity)
    }

    fun editReminder(reminderEntity : ReminderEntity) {
        remindersRepo.editReminder(reminderEntity)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}