package com.example.androidgaya.repositories.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidgaya.repositories.models.ReminderEntity

@Dao
interface RemindersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReminder(reminderEntity: ReminderEntity)

    @Update
    fun updateReminder(reminderEntity: ReminderEntity)

    @Delete
    fun deleteReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE user LIKE :userId ORDER BY createdAt ASC")
    fun getRemindersByUserId(userId: String): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id LIKE :id")
    fun getReminderByID(id: Int): ReminderEntity?

    @Query("SELECT id FROM reminders WHERE user LIKE :userId ORDER BY createdAt ASC")
    fun getMyRemindersIds(userId: String): List<Int>

    @Query("DELETE FROM reminders")
    fun deleteAllReminders()
}