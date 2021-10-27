package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.ReminderEntity

interface ReminderInterface {
    fun deleteReminderFromDB(reminderEntity: ReminderEntity)
    fun getRemindersFromDB(userId: String): LiveData<List<ReminderEntity>?>
    fun getRemindersByUsernameList(userId: String): List<ReminderEntity>
    fun addReminderToDB(reminderEntity: ReminderEntity)
    fun editReminderFromDB(reminderEntity: ReminderEntity)
    fun getReminderByID(id: Int): ReminderEntity?
    fun getMyRemindersIds(userId: String): List<Int>
    fun deleteAllReminders()
    fun getAllReminders(context: Context, userId: String)
    fun createReminder(context: Context, reminderEntity: ReminderEntity)
    fun editReminder(context: Context, reminderEntity: ReminderEntity)
    fun deleteReminder(context: Context, reminderEntity: ReminderEntity)
    fun logout(context: Context, userId: String)
    fun onDeleteReminder(context: Context, reminderEntity: ReminderEntity)
    fun onEditReminder(context: Context, reminderEntity: ReminderEntity)
    fun onCreateReminder(context: Context, reminderEntity: ReminderEntity)
}