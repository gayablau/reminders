package com.example.androidgaya.reminders.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.repositories.models.Reminder
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils
import java.util.*

class RemindersViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    fun deleteReminderById(id: Int, username: String, activity: Activity) {
        remindersRepo.deleteReminderById(id, username)
        NotificationUtils().deleteNotification(activity, id)
    }

    fun getRemindersByUsername(username: String) : ArrayList<Reminder>? {
        return remindersRepo.getRemindersByUsername(username)
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }


}