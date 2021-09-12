package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import javax.inject.Inject

class SocketService : Service() {
    private lateinit var serviceLooper: Looper
    private lateinit var serviceHandler: ServiceHandler
    private lateinit var remindersRepo: RemindersRepo
    private lateinit var loggedInUserRepo: LoggedInUserRepo
    private lateinit var userRepo: UserRepo

    @Inject
    lateinit var socket: SocketRepo

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            socket.onCreateReminder(remindersRepo)
            socket.onEditReminder(remindersRepo)
            socket.onDeleteReminder(remindersRepo)
            socket.onCreateUser(userRepo)
            socket.onChangeUsername(userRepo, loggedInUserRepo)
            socket.onGetAllUsers(userRepo)
            socket.onGetAllReminders(remindersRepo)
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
