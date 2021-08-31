package com.example.androidgaya.repositories.interfaces

import androidx.lifecycle.LiveData
import com.example.androidgaya.repositories.models.ReminderEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

interface ReminderInterface {
    fun deleteReminder(reminderEntity: ReminderEntity)
    fun getReminders(userId: Int) : LiveData<List<ReminderEntity>?>
    fun getRemindersByUsernameList(userId : Int) : List<ReminderEntity>
    fun addReminder(reminderEntity : ReminderEntity)
    fun editReminder(reminderEntity : ReminderEntity)
    fun getReminderByID(id : Int) : ReminderEntity?
    fun getMyRemindersIds(userId : Int) : List<Int>
    fun deleteAllReminders()
}