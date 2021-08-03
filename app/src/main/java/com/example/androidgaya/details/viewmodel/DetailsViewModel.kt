package com.example.androidgaya.details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import javax.inject.Inject

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var remindersDao: RemindersDao
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectDetails(this)
    }

    fun addReminder(reminderEntity : ReminderEntity) {
        remindersDao.addReminder(reminderEntity)
    }

    fun editReminder(reminderEntity : ReminderEntity) {
        //remindersDao.editReminder(id, reminderEntity.header, reminderEntity.description, reminderEntity.time)
        remindersDao.updateReminder(reminderEntity)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersDao.getReminderByID(id)
    }

/*    fun getLiveDetails(username: String?) {
        val list = remindersDao.getRemindersByUsername(username)
        remindersList.postValue(list)
    }*/
}