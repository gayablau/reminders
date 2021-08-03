package com.example.androidgaya.reminders.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils
import javax.inject.Inject

class RemindersViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var remindersDao: RemindersDao
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)
    private var remindersList: MutableLiveData<ArrayList<ReminderEntity>?>
    private var username: String?

    init {
        (application as AppDataGetter).getAppComponent()?.injectReminders(this)
        remindersList = MutableLiveData()
        username = getUsername()
        getMyReminders(username)
    }

    fun deleteReminderById(id: Int, activity: Activity) {
  /*      var reminder =  ReminderEntity(id, "", "", "", 0)
        remindersDao.deleteReminder(reminder)*/
        remindersDao.deleteReminderById(id)
        NotificationUtils().deleteNotification(activity, id)
    }

//    fun getRemindersByUsername(username: String) : MutableLiveData<ArrayList<ReminderEntity>>? {
//        return remindersDao.getRemindersByUsername(username) as MutableLiveData<ArrayList<ReminderEntity>>?
//    }

    fun getRemindersByUsername(username: String) : ArrayList<ReminderEntity>? {
        return remindersDao.getRemindersByUsername(username) as ArrayList<ReminderEntity>?
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun getMyReminders(username: String?) {
        if (username != null) {
            val list = getRemindersByUsername(username)
            remindersList.postValue(list)
        }
    }

    fun getRemindersObserver(): MutableLiveData<ArrayList<ReminderEntity>?> {
        return remindersList
    }
}