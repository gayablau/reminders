package com.example.androidgaya.repositories.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidgaya.repositories.models.ReminderEntity

@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminders ORDER BY createdAt ASC")
    fun getAllRemindersFromDB(): LiveData<List<ReminderEntity>>

    @Insert
    fun addReminder(reminderEntity: ReminderEntity)

    @Update
    fun updateReminder(reminderEntity: ReminderEntity)

    @Delete
    fun deleteReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE user LIKE :userId ORDER BY createdAt ASC")
    fun getRemindersByUserId(userId: String): LiveData<List<ReminderEntity>?>

    @Query("SELECT * FROM reminders WHERE user LIKE :userId ORDER BY createdAt ASC")
    fun getRemindersByUserIdList(userId: String): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE user LIKE :username ORDER BY createdAt ASC")
    fun getRemindersByUsernameList(username: String): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id LIKE :id")
    fun getReminderByID(id: Int): ReminderEntity?

    @Query("UPDATE reminders SET header = :header AND description = :description AND time = :time WHERE id = :id")
    fun editReminder(id: Int, header: String, description: String, time: Long)

    @Query("DELETE FROM reminders WHERE id = :id")
    fun deleteReminderById(id: Int)

    @Query("SELECT id FROM reminders WHERE user LIKE :userId ORDER BY createdAt ASC")
    fun getMyRemindersIds(userId: String): List<Int>

    @Query("DELETE FROM reminders")
    fun deleteAllReminders()
}