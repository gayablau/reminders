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
    suspend fun createReminder(context: Context, reminderEntity: ReminderEntity)
    suspend fun editReminder(context: Context, reminderEntity: ReminderEntity)
    suspend fun deleteReminder(context: Context, reminderEntity: ReminderEntity)
    fun logout(context: Context, userId: String)
    suspend fun onDeleteReminder(context: Context, reminderEntity: ReminderEntity)
    suspend fun onEditReminder(context: Context, reminderEntity: ReminderEntity)
    suspend fun onCreateReminder(context: Context, reminderEntity: ReminderEntity)
    suspend fun onCreateReminders(context: Context, reminderEntity: ReminderEntity)
}