package com.example.androidgaya.repositories.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.repositories.models.ReminderEntity

interface ReminderInterface {
    fun getRemindersFromDB(userId: String): LiveData<List<ReminderEntity>?>
    fun getReminderByID(id: Int): ReminderEntity?
    fun getMyRemindersIds(userId: String): List<Int>
    fun deleteAllReminders()
    fun getAllReminders(context: Context, userId: String)
    fun createReminder(context: Context, reminderEntity: ReminderEntity)
    fun editReminder(context: Context, reminderEntity: ReminderEntity)
    fun deleteReminder(context: Context, reminderEntity: ReminderEntity)
}