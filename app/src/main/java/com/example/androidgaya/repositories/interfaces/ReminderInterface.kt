package com.example.androidgaya.repositories.interfaces

import com.example.androidgaya.repositories.models.Reminder
import java.util.*

interface ReminderInterface {
    fun getReminderByID(id: Int, username: String): Reminder?
    fun getRemindersMap(): Map<String, ArrayList<Reminder>>?
    fun getRemindersByUsername(username: String): ArrayList<Reminder>?
    fun isUsernameExists(username: String): Boolean
    fun addUsername(username: String)
    fun editUsername(oldUsername: String, newUsername: String)
    fun addReminder(reminder: Reminder, username: String)
    fun editReminder(updatedReminder: Reminder, username: String)
    fun deleteReminderById(id: Int, username: String)
}