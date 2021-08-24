package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import com.example.androidgaya.R
import com.example.androidgaya.login.ui.LoginActivity
import com.example.androidgaya.main.interfaces.MainActivityInterface
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import com.google.android.material.internal.ContextUtils.getActivity
import io.socket.client.Socket
import javax.inject.Inject

class SocketService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var remindersRepo : RemindersRepo
    private lateinit var loggedInUserRepo : LoggedInUserRepo
    private lateinit var userRepo: UserRepo
    @set:Inject
    var mSocket: Socket? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                mSocket?.on("createReminder") { args ->
                    if (args[0] != null)
                    {
                        val rem  = ReminderEntity(args[0] as Int,
                                args[1] as String?,
                                args[2] as String?,
                                args[3] as String?,
                                args[4] as Long,
                                args[5] as Long)
                        remindersRepo.addReminder(rem)

                        // NotificationUtils().setNotification(rem.time, getActivity(), rem.header, rem.description, rem.id)
                    }
                }
                mSocket?.on("editReminder") { args ->
                    if (args[0] != null)
                    {
                        val rem  = ReminderEntity(args[0] as Int,
                                args[1] as String?,
                                args[2] as String?,
                                args[3] as String?,
                                args[4] as Long,
                                args[5] as Long)
                        remindersRepo.editReminder(rem)

                        // NotificationUtils().setNotification(rem.time, getActivity(), rem.header, rem.description, rem.id)
                    }
                }
                mSocket?.on("deleteReminder") { args ->
                    if (args[0] != null)
                    {
                        val rem  = ReminderEntity(args[0] as Int,
                                args[1] as String?,
                                args[2] as String?,
                                args[3] as String?,
                                args[4] as Long,
                                args[5] as Long)
                        remindersRepo.deleteReminder(rem)

                        // NotificationUtils().setNotification(rem.time, getActivity(), rem.header, rem.description, rem.id)
                    }
                }
                /*mSocket?.on("updateUsers") { args ->
                    if (args[0] != null && args[1] != null)
                    {
                        val usernames = args[0] as List<*>
                        val passwords = args[1] as List<*>
                        var i = 0
                        for(username in usernames) {
                            var user = UserEntity(username as String, passwords[i] as String)
                            i++
                            userRepo.insertUser(user)
                        }

                        val rem  = ReminderEntity(args[0] as Int,
                                args[1] as String?,
                                args[2] as String?,
                                args[3] as String?,
                                args[4] as Long,
                                args[5] as Long)
                        remindersRepo.deleteReminder(rem)

                        // NotificationUtils().setNotification(rem.time, getActivity(), rem.header, rem.description, rem.id)
                    }
                }*/
                mSocket?.on("createUser") { args ->
                    if (args[0] != null)
                    {
                        val user = UserEntity(args[0] as String, args[1] as String)
                        userRepo.insertUser(user)
                    }
                }
                mSocket?.on("changeUsername") { args ->
                    if (args[0] != null)
                    {
                        userRepo.editUsername(args[0] as String, args[1] as String)
                        if (loggedInUserRepo.getUsername(application).equals(args[0] as String)) {
                            loggedInUserRepo.setUsername(application, args[1] as String)
                            //(applicationContext as MainActivityInterface).changeToolbar(getString(R.string.toolbar_main, args[1] as String), false)
                        }
                    }
                }
            } catch (ex : Exception) {}

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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}
