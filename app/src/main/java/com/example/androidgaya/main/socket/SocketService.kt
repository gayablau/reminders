package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class SocketService : Service() {
    private lateinit var serviceLooper: Looper
    private lateinit var serviceHandler: ServiceHandler
    private lateinit var remindersRepo : RemindersRepo
    private lateinit var loggedInUserRepo : LoggedInUserRepo
    private lateinit var userRepo: UserRepo
    @set:Inject
    var mSocket: Socket? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {

            } catch (ex: Exception) {}

            mSocket?.on("createReminder") { args ->
                if (args[0] != null)
                {
                    val rem  = ReminderEntity(args[0] as Int,
                            args[1] as String,
                            args[2] as String?,
                            args[3] as Int,
                            args[4] as Long,
                            args[5] as Long)
                    if (remindersRepo.getReminderByID(args[0] as Int) == null) {
                        remindersRepo.addReminder(rem)
                    }
                    NotificationUtils().setNotification(rem.time, this@SocketService, rem.header, rem.description, rem.id)
                }
            }
            mSocket?.on("editReminder") { args ->
                if (args[0] != null)
                {
                    val rem  = ReminderEntity(args[0] as Int,
                            args[1] as String,
                            args[2] as String?,
                            args[3] as Int,
                            args[4] as Long,
                            args[5] as Long)
                    if (remindersRepo.getReminderByID(args[0] as Int) != null) {
                        remindersRepo.editReminder(rem)
                    }
                    else {
                        remindersRepo.addReminder(rem)
                    }
                    NotificationUtils().setExistNotification(rem.time, this@SocketService, rem.header, rem.description, rem.id)
                }
            }
            mSocket?.on("deleteReminder") { args ->
                if (args[0] != null)
                {
                    val rem  = ReminderEntity(args[0] as Int,
                            args[1] as String,
                            args[2] as String?,
                            args[3] as Int,
                            args[4] as Long,
                            args[5] as Long)
                    remindersRepo.deleteReminder(rem)

                    NotificationUtils().deleteNotification(this@SocketService, rem.id)
                }
            }
            mSocket?.on("createUser") { args ->
                if (args[0] != null) {
                    val user = UserEntity(args[0] as Int, args[1] as String, args[2] as String)
                    userRepo.insertUser(user)
                }
            }
            mSocket?.on("changeUsername") { args ->
                if (args[0] != null) {
                    mSocket!!.emit("changeUsernameOnly", args[1] as String)
                    userRepo.editUsername(args[0] as String, args[1] as String)
                    if (loggedInUserRepo.getLoggedInUsername(application).equals(args[0] as String)) {
                        loggedInUserRepo.setLoggedInUsername(application, args[1] as String)
                    }
                }
            }
            mSocket?.on("getAllUsers") { args ->
                if (args[0] != null) {
                    val data = args[0] as JSONArray
                    for (i in 0 until data.length()) {
                        if (!userRepo.isUserExists(data.getJSONObject(i).get("username") as String)) {
                            val userEntity = UserEntity(data.getJSONObject(i).get("userId") as Int, data.getJSONObject(i).get("username") as String, data.getJSONObject(i).get("password") as String)
                            userRepo.insertUser(userEntity)
                        }
                    }
                }
            }
            mSocket?.on("getAllReminders") { args ->
                if (args[0] != null) {
                    val data = args[0] as JSONArray
                    for (i in 0 until data.length()) {
                        if (data.getJSONObject(i).get("username") as String == loggedInUserRepo.getLoggedInUsername(application)) {
                            val reminders = data.getJSONObject(i).get("reminders") as JSONArray
                            val userId = data.getJSONObject(i).get("userId") as Int
                            for (i in 0 until reminders.length()) {
                                val reminder = reminders[i] as JSONObject
                                if (remindersRepo.getReminderByID(reminder.getInt("id")) == null) {
                                    val remToAdd = ReminderEntity(reminder.getInt("id"),
                                            reminder.getString("header"),
                                            reminder.getString("description"),
                                            userId,
                                            reminder.getLong("time"),
                                            reminder.getLong("createdAt"))
                                    remindersRepo.addReminder(remToAdd)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        (application as AppDataGetter).getAppComponent()?.injectSocketService(this)
        remindersRepo = RemindersRepo(application)
        loggedInUserRepo = LoggedInUserRepo(application)
        userRepo = UserRepo(application)
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments").apply {


            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler.obtainMessage().also { msg ->
            msg.arg1 = startId
            serviceHandler.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}
