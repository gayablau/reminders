package com.example.androidgaya.repositories.reminder

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.multidex.MultiDexApplication
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.ReminderInterface
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.util.NotificationUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RemindersRepo(application: Application) : ReminderInterface {
    @Inject
    lateinit var remindersDao: RemindersDao

    init {
        (application as AppDataGetter).getAppComponent()?.injectRemindersRepo(this)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) = runBlocking {
        launch {  remindersDao.deleteReminder(reminderEntity) }
    }

    fun getReminders(userId: Int) : LiveData<List<ReminderEntity>?> {
        return remindersDao.getRemindersByUserId(userId)
    }

    fun getRemindersByUsernameList(userId : Int) : List<ReminderEntity> {
        return remindersDao.getRemindersByUserIdList(userId)
    }

    fun addReminder(reminderEntity : ReminderEntity) = runBlocking {
        launch { remindersDao.addReminder(reminderEntity) }
    }

    fun editReminder(reminderEntity : ReminderEntity) = runBlocking {
        launch { remindersDao.updateReminder(reminderEntity) }
    }

    fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersDao.getReminderByID(id)
    }

    fun getMyRemindersIds(userId : Int) : List<Int> {
        return  remindersDao.getMyRemindersIds(userId)
    }

    fun deleteAllReminders() = runBlocking {
        launch {
            remindersDao.deleteAllReminders()
        }
    }
}