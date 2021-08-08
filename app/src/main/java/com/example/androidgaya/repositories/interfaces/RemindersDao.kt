package com.example.androidgaya.repositories.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminders")
    fun getAllRemindersFromDB(): List<ReminderEntity>?

    @Insert
    fun addReminder(reminderEntity: ReminderEntity)

    @Update
    fun updateReminder(reminderEntity: ReminderEntity)

    @Delete
    fun deleteReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE username LIKE :username ORDER BY createdAt ASC")
    fun getRemindersByUsername(username: String): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id LIKE :id")
    fun getReminderByID(id: Int): ReminderEntity?

    @Query("UPDATE reminders SET username = :newUsername WHERE username = :oldUsername")
    fun editUsername(oldUsername: String, newUsername: String)

    @Query("UPDATE reminders SET header = :header AND description = :description AND time = :time WHERE id = :id")
    fun editReminder(id: Int, header: String, description: String, time: Long)

    @Query("DELETE FROM reminders WHERE id = :id")
    fun deleteReminderById(id: Int)
}