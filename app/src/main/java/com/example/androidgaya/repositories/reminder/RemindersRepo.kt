package com.example.androidgaya.repositories.reminder

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.ReminderInterface
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.util.NotificationUtils
import javax.inject.Inject

class RemindersRepo(application: Application) : ReminderInterface {
    @Inject
    lateinit var remindersDao: RemindersDao

    init {
        (application as AppDataGetter).getAppComponent()?.injectRemindersRepo(this)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersDao.deleteReminder(reminderEntity)
    }

    fun getRemindersByUsername(username: String) : ArrayList<ReminderEntity>? {
        return remindersDao.getRemindersByUsername(username) as ArrayList<ReminderEntity>?
    }

    fun editUsername(oldUsername: String, newUsername: String) {
        remindersDao.editUsername(oldUsername, newUsername)
    }

    fun addReminder(reminderEntity : ReminderEntity) {
        remindersDao.addReminder(reminderEntity)
    }

    fun editReminder(reminderEntity : ReminderEntity) {
        remindersDao.updateReminder(reminderEntity)
    }

    fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersDao.getReminderByID(id)
    }
}