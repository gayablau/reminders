package com.example.androidgaya.socket

import android.app.Service
import android.content.Intent
import android.os.*
import com.example.androidgaya.R
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.notifications.NotificationUtils
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocketService : Service() {
    @Inject
    lateinit var remindersDao: RemindersDao

    @Inject
    lateinit var loggedInUserDao: LoggedInUserDao

    @Inject
    lateinit var socketDao: SocketDao

    @Inject
    lateinit var reminderEntityAdapter: JsonAdapter<ReminderEntity>

    private val socketCoroutineJob = SupervisorJob()
    private val socketScope = CoroutineScope(Dispatchers.IO + socketCoroutineJob)

    override fun onCreate() {
        (application as ReminderApplication).getAppComponent()?.injectSocketService(this)
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
            socketScope.launch {
                if (args[0] != null) {
                    val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                    if (reminder != null) {
                        remindersDao.addReminder(reminder)
                        NotificationUtils().setNotification(application, reminder)
                    }
                }
            }
        }
    }

    private fun onEditReminder() {
        socketDao.listen(getString(R.string.on_edit_reminder)) { args ->
            socketScope.launch {
                if (args[0] != null) {
                    val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                    if (reminder != null) {
                        if (remindersDao.getReminderByID(reminder.id) != null) {
                            remindersDao.updateReminder(reminder)
                            NotificationUtils().setExistNotification(application, reminder)
                        } else {
                            remindersDao.addReminder(reminder)
                            NotificationUtils().setNotification(application, reminder)
                        }
                    }
                }
            }
        }
    }

    private fun onDeleteReminder() {
        socketDao.listen(getString(R.string.on_delete_reminder)) { args ->
            socketScope.launch {
                if (args[0] != null) {
                    val reminder = reminderEntityAdapter.fromJson(args[0].toString())
                    if (reminder != null) {
                        if (remindersDao.getReminderByID(reminder.id) != null) {
                            remindersDao.deleteReminder(reminder)
                            NotificationUtils().deleteNotification(application, reminder)
                        }
                    }
                }
            }
        }
    }

    private fun onChangeUsername() {
        socketDao.listen(getString(R.string.on_change_username)) { args ->
            socketScope.launch {
                if (args[0] != null) {
                    val user = LoggedInUserEntity(args[0].toString(), args[1].toString())
                    loggedInUserDao.updateLoggedInUser(user)
                }
            }
        }
    }
}
