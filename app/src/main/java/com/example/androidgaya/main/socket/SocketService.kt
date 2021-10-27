package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.types.UserJson
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


class SocketService : Service() {
    private lateinit var serviceLooper: Looper
    private lateinit var serviceHandler: ServiceHandler
    private lateinit var remindersRepo: RemindersRepo
    private lateinit var loggedInUserRepo: LoggedInUserRepo
    private lateinit var userRepo: UserRepo
    private val mBinder: IBinder = SocketBinder()

    @Inject
    lateinit var socketDao: SocketDao

    @Inject
    lateinit var reminderEntityAdapter: JsonAdapter<ReminderEntity>

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            onCreateReminder(remindersRepo)
            onEditReminder(remindersRepo)
            onDeleteReminder(remindersRepo)
            onChangeUsername(userRepo, loggedInUserRepo)
        }
    }

    override fun onCreate() {
        (application as AppDataGetter).getAppComponent()?.injectSocketService(this)
        remindersRepo = RemindersRepo(application)
        loggedInUserRepo = LoggedInUserRepo(application)
        userRepo = UserRepo(application)

        HandlerThread(getString(R.string.Service_start_arguments)).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler.obtainMessage().also { msg ->
            msg.arg1 = startId
            serviceHandler.sendMessage(msg)
        }
        return START_STICKY
    }

    inner class SocketBinder : Binder() {
        fun getService() : SocketService {
            return this@SocketService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }


    fun onCreateReminder(remindersRepo: RemindersRepo) {
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

    fun onEditReminder(remindersRepo: RemindersRepo) {
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

    fun onDeleteReminder(remindersRepo: RemindersRepo) {
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

    fun onChangeUsername(userRepo: UserRepo, loggedInUserRepo: LoggedInUserRepo) {
        socketDao.listen(getString(R.string.on_change_username)) { args ->
            if (args[0] != null) {
                loggedInUserRepo.setLoggedIn(loggedInUserRepo.getLoggedInUserId(application),
                        args[0] as String)
            }
        }
    }
}
