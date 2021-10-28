package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.squareup.moshi.JsonAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


class SocketService : Service() {
    @Inject
    lateinit var remindersRepo: RemindersRepo

    @Inject
    lateinit var loggedInUserRepo: LoggedInUserRepo

    @Inject
    lateinit var socketDao: SocketDao

    @Inject
    lateinit var reminderEntityAdapter: JsonAdapter<ReminderEntity>

    override fun onCreate() {
        (application as AppDataGetter).getAppComponent()?.injectSocketService(this)
        onCreateReminder()
        onEditReminder()
        onDeleteReminder()
        onChangeUsername()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun onCreateReminder() {
        socketDao.listen(getString(R.string.on_create_reminder)) { args ->
            if (args[0] != null) {
                val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                if (reminder != null) {
                    if (remindersRepo.getReminderByID(reminder.id) == null) {
                        remindersRepo.onCreateReminder(application, reminder)
                    }
                }
            }
        }
    }

    private fun onEditReminder() {
        socketDao.listen(getString(R.string.on_edit_reminder)) { args ->
            if (args[0] != null) {
                val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                if (reminder != null) {
                    if (remindersRepo.getReminderByID(reminder.id) != null) {
                        remindersRepo.onEditReminder(application, reminder)
                    } else {
                        remindersRepo.onCreateReminder(application, reminder)
                    }
                }
            }
        }
    }

    private fun onDeleteReminder() {
        socketDao.listen(getString(R.string.on_delete_reminder)) { args ->
            if (args[0] != null) {
                val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                if (reminder != null) {
                    if (remindersRepo.getReminderByID(reminder.id) != null) {
                        remindersRepo.onDeleteReminder(application, reminder)
                    }
                }
            }
        }
    }

    private fun onChangeUsername() {
        socketDao.listen(getString(R.string.on_change_username)) { args ->
            if (args[0] != null) {
                loggedInUserRepo.setLoggedIn(loggedInUserRepo.getLoggedInUserId(application),
                        args[0] as String)
            }
        }
    }
}
