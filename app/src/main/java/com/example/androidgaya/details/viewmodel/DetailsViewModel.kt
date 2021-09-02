package com.example.androidgaya.details.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var remindersRepo : RemindersRepo = RemindersRepo(application)
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)
    private var userRepo : UserRepo = UserRepo(application)

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectDetails(this)
    }

    fun addReminder(reminderEntity : ReminderEntity) {
        remindersRepo.addReminder(reminderEntity)
        mSocket!!.emit((getApplication() as Context).getString(R.string.create_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun editReminder(reminderEntity : ReminderEntity) {
        remindersRepo.editReminder(reminderEntity)
        mSocket!!.emit((getApplication() as Context).getString(R.string.edit_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun getUsername() : String {
        return loggedInUserRepo.getLoggedInUsername(getApplication())
    }

    fun getUserId() : Int {
        return loggedInUserRepo.getLoggedInUserId(getApplication())
    }

    fun getReminderByID(id : Int) : ReminderEntity? {
        return remindersRepo.getReminderByID(id)
    }
}