package com.example.androidgaya.repositories.reminder

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.ReminderInterface
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RemindersRepo(application: Application) : ReminderInterface {
    @Inject
    lateinit var remindersDao: RemindersDao

    init {
        (application as AppDataGetter).getAppComponent()?.injectRemindersRepo(this)
    }

    override fun deleteReminder(reminderEntity: ReminderEntity): Unit = runBlocking {
        launch {  remindersDao.deleteReminder(reminderEntity) }
    }

    override fun getReminders(userId: Int) : LiveData<List<ReminderEntity>?> {
        return remindersDao.getRemindersByUserId(userId)
    }

    override fun getRemindersByUsernameList(userId : Int) : List<ReminderEntity> {
        return remindersDao.getRemindersByUserIdList(userId)
    }

    override fun addReminder(reminderEntity : ReminderEntity): Unit = runBlocking {
        launch { remindersDao.addReminder(reminderEntity) }
    }

    override fun editReminder(reminderEntity : ReminderEntity): Unit = runBlocking {
        launch { remindersDao.updateReminder(reminderEntity) }
    }

    override fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersDao.getReminderByID(id)
    }

    override fun getMyRemindersIds(userId : Int) : List<Int> {
        return  remindersDao.getMyRemindersIds(userId)
    }

    override fun deleteAllReminders(): Unit = runBlocking {
        launch {
            remindersDao.deleteAllReminders()
        }
    }
}