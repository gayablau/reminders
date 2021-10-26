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
    lateinit var socket: SocketDao

    @Inject
    lateinit var moshi: Moshi

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
           // onCreateReminder(remindersRepo)
           // onEditReminder(remindersRepo)
            //onDeleteReminder(remindersRepo)
           // onCreateUser(userRepo)
           // onChangeUsername(userRepo, loggedInUserRepo)
           // onGetAllReminders(remindersRepo)
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


   /* fun createReminder(reminderEntity: ReminderEntity) {
        //val reminder = jsonReminderAdapter.toJson(reminderEntity)

        Log.d("idk", reminderEntity.toString())

        socket.emit(getString(R.string.create_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        socket.emit(getString(R.string.edit_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        socket.emit(getString(R.string.delete_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun logout() {
        socket.emit(getString(R.string.logout))
    }

    fun getAllReminders(userId: Int) {
        socket.emit(getString(R.string.get_all_reminders), userId)
    }

    fun changeUsername(oldUsername: String, newUsername: String) {
        socket.emit(getString(R.string.change_username),
                oldUsername,
                newUsername)
    }

    fun onCreateReminder(remindersRepo: RemindersRepo) {
        socket.listen(getString(R.string.create_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as String,
                        args[4] as Long,
                        args[5] as Long)
                if (remindersRepo.getReminderByID(args[0] as Int) == null) {
                    remindersRepo.addReminder(rem)
                    NotificationUtils().setNotification(rem.time,
                            applicationContext,
                            rem.header,
                            rem.description,
                            rem.id)
                }
            }
        }

    }

    fun onEditReminder(remindersRepo: RemindersRepo) {
        socket.listen(getString(R.string.edit_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as String,
                        args[4] as Long,
                        args[5] as Long)
                if (remindersRepo.getReminderByID(args[0] as Int) != null) {
                    remindersRepo.editReminder(rem)
                    NotificationUtils().setExistNotification(rem.time,
                            applicationContext,
                            rem.header,
                            rem.description,
                            rem.id)
                } else {
                    remindersRepo.addReminder(rem)
                    NotificationUtils().setNotification(rem.time,
                            applicationContext,
                            rem.header,
                            rem.description,
                            rem.id)
                }
            }
        }
    }

    fun onDeleteReminder(remindersRepo: RemindersRepo) {
        socket.listen(application.getString(R.string.delete_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as String,
                        args[4] as Long,
                        args[5] as Long)
                remindersRepo.deleteReminder(rem)
                NotificationUtils().deleteNotification(application.applicationContext, rem.id)
            }
        }
    }

    fun onCreateUser(userRepo: UserRepo) {
        socket.listen(application.getString(R.string.create_user)) { args ->
            if (args[0] != null) {
                val user = UserEntity(args[0] as String, args[1] as String, args[2] as String)
                userRepo.insertUser(user)
            }
        }
    }

    fun onChangeUsername(userRepo: UserRepo, loggedInUserRepo: LoggedInUserRepo) {
        socket.listen(application.getString(R.string.change_username)) { args ->
            if (args[0] != null) {
                userRepo.editUsername(args[0] as String, args[1] as String)
                if (loggedInUserRepo.getLoggedInUsername(application) == args[0] as String) {
                    loggedInUserRepo.setLoggedIn(application,
                            loggedInUserRepo.getLoggedInUserId(application),
                            args[1] as String)
                }
            }
        }
    }

    fun onGetAllUsers(userRepo: UserRepo) {
        socket.listen(application.getString(R.string.get_all_users)) { args ->
            if (args[0] != null) {
                val data = args[0] as JSONArray
                val usersList = jsonUsersAdapter.fromJson(data.toString())
                if (usersList != null) {
                    for (user in usersList) {
                        if (!userRepo.isUserExists(user.username)) {
                            val userToAdd = jsonToUserEntity(user)
                            userRepo.insertUser(userToAdd)
                        }
                    }
                }
            }
        }
    }

    fun onGetAllReminders(remindersRepo: RemindersRepo) {
        socket.listen(application.getString(R.string.get_all_reminders)) { args ->
            if (args[0] != null) {
                remindersRepo.deleteAllReminders()
                val reminders = args[0] as JSONArray
                val remindersList = jsonRemindersAdapter.fromJson(reminders.toString())
                if (remindersList != null) {
                    for (rem in remindersList) {
                        if (remindersRepo.getReminderByID(rem.id) == null) {
                            val remToAdd = jsonToRemEntity(rem)
                            remindersRepo.addReminder(remToAdd)
                            NotificationUtils().setExistNotification(remToAdd.time,
                                    application.applicationContext,
                                    remToAdd.header,
                                    remToAdd.description,
                                    remToAdd.id)
                        }
                    }
                }
            }
        }
    }

    private fun jsonToRemEntity(reminderJson: ReminderJson): ReminderEntity {
        return ReminderEntity(reminderJson.id,
                reminderJson.header,
                reminderJson.description,
                reminderJson.user,
                reminderJson.time,
                reminderJson.createdAt)
    }

    private fun jsonToUserEntity(userJson: UserJson): UserEntity {
        return UserEntity(userJson.userId,
                userJson.username,
                userJson.password)
    }*/
}
