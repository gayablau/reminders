package com.example.androidgaya.reminders.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDexApplication
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    //private var remindersList: MutableLiveData<ArrayList<ReminderEntity>?>
    private var username: String? = null

    val remindersList: MutableLiveData<ArrayList<ReminderEntity>?> by lazy {
        MutableLiveData<ArrayList<ReminderEntity>?>()
    }

    init {
        (application as AppDataGetter).getAppComponent()?.injectReminders(this)
        //remindersList = MutableLiveData<ArrayList<ReminderEntity>?>()
        username = getUsername()
        postMyReminders(username)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersRepo.deleteReminder(reminderEntity)
    }


    fun getRemindersByUsername(username: String) : ArrayList<ReminderEntity>? {
        return remindersRepo.getRemindersByUsername(username) as ArrayList<ReminderEntity>?
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

/*    fun getMyReminders(username: String?) {
        if (username != null) {
            val list = getRemindersByUsername(username)
            remindersList.postValue(list)
            Log.i("getMyReminders", list.toString())
            Log.i("getMyReminders", remindersList.value.toString())
        }
    }*/

    fun getMyReminders(username: String?): ArrayList<ReminderEntity>? {
        if (username != null) {
            return getRemindersByUsername(username);
        }
        return null
    }

    fun postMyReminders(username: String?) {
        remindersList.postValue(getMyReminders(username))
    }

    fun getRemindersObserver(): MutableLiveData<ArrayList<ReminderEntity>?> {
        return remindersList
    }
}