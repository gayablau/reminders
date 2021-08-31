package com.example.androidgaya.main.socket

import android.app.Service
import android.content.Intent
import android.os.*
import com.example.androidgaya.R
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.types.UserJson
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.socket.client.Socket
import org.json.JSONArray
import javax.inject.Inject

class SocketService : Service() {
    private lateinit var serviceLooper: Looper
    private lateinit var serviceHandler: ServiceHandler
    private lateinit var remindersRepo : RemindersRepo
    private lateinit var loggedInUserRepo : LoggedInUserRepo
    private lateinit var userRepo: UserRepo
    @set:Inject
    var mSocket: Socket? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {

            } catch (ex: Exception) {}

            mSocket.on(getString(R.string.create_reminder)) { args ->
                if (args[0] != null) {
                    val rem  = ReminderEntity(args[0] as Int,
                            args[1] as String,
                            args[2] as String?,
                            args[3] as Int,
                            args[4] as Long,
                            args[5] as Long)
                    if (remindersRepo.getReminderByID(args[0] as Int) == null) {
                        remindersRepo.addReminder(rem)
                        NotificationUtils().setNotification(rem.time, this@SocketService, rem.header, rem.description, rem.id)
                    }
                }
            }
            mSocket.on(getString(R.string.edit_reminder)) { args ->
                if (args[0] != null) {
                    val rem  = ReminderEntity(args[0] as Int,
                            args[1] as String,
                            args[2] as String?,
                            args[3] as Int,
                            args[4] as Long,
                            args[5] as Long)
                    if (remindersRepo.getReminderByID(args[0] as Int) != null) {
                        remindersRepo.editReminder(rem)
                        NotificationUtils().setExistNotification(rem.time, this@SocketService, rem.header, rem.description, rem.id)
                    } else {
                        remindersRepo.addReminder(rem)
                        NotificationUtils().setNotification(rem.time, this@SocketService, rem.header, rem.description, rem.id)
                    }
                }
            }
            mSocket.on(getString(R.string.delete_reminder)) { args ->
                if (args[0] != null) {
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
            mSocket.on(getString(R.string.create_user)) { args ->
                if (args[0] != null) {
                    val user = UserEntity(args[0] as Int, args[1] as String, args[2] as String)
                    userRepo.insertUser(user)
                }
            }
            mSocket.on(getString(R.string.change_username)) { args ->
                if (args[0] != null) {
                    mSocket!!.emit(getString(R.string.change_username_only), args[1] as String)
                    userRepo.editUsername(args[0] as String, args[1] as String)
                    if (loggedInUserRepo.getLoggedInUsername(application).equals(args[0] as String)) {
                        loggedInUserRepo.setLoggedInUsername(application, args[1] as String)
                    }
                }
            }
            mSocket.on(getString(R.string.get_all_users)) { args ->
                if (args[0] != null) {
                    val data = args[0] as JSONArray
                    val moshi : Moshi = Moshi.Builder().build()
                    val listMyData = Types.newParameterizedType(List::class.java, UserJson::class.java)
                    val jsonAdapter = moshi.adapter<List<UserJson>>(listMyData)
                    val usersList = jsonAdapter.fromJson(data.toString())
                    if (usersList != null) {
                        for(user in usersList) {
                            if (!userRepo.isUserExists(user.username)) {
                                val userToAdd = jsonToUserEntity(user)
                                userRepo.insertUser(userToAdd)
                            }
                        }
                    }

                }
            }

            mSocket.on(getString(R.string.get_all_reminders)) { args ->
                if (args[0] != null) {
                    remindersRepo.deleteAllReminders()
                    val reminders = args[0] as JSONArray
                    val moshi : Moshi = Moshi.Builder().build()
                    val listMyData = Types.newParameterizedType(List::class.java, ReminderJson::class.java)
                    val jsonAdapter = moshi.adapter<List<ReminderJson>>(listMyData)
                    val remindersList = jsonAdapter.fromJson(reminders.toString())
                    if (remindersList != null) {
                        for(rem in remindersList) {
                            if (remindersRepo.getReminderByID(rem.id) == null) {
                                val remToAdd = jsonToRemEntity(rem)
                                remindersRepo.addReminder(remToAdd)
                                NotificationUtils().setExistNotification(remToAdd.time,
                                        this@SocketService,
                                        remToAdd.header,
                                        remToAdd.description,
                                        remToAdd.id)
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

    fun jsonToRemEntity(reminderJson: ReminderJson): ReminderEntity {
        return ReminderEntity(reminderJson.id,
                reminderJson.header,
                reminderJson.description,
                reminderJson.user,
                reminderJson.time,
                reminderJson.createdAt)
    }

    fun jsonToUserEntity(userJson: UserJson): UserEntity {
        return UserEntity(userJson.sharedId,
                userJson.username,
                userJson.password)
    }
}
