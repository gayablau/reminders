package com.example.androidgaya.repositories.socket

import android.app.Application
import com.example.androidgaya.R
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.types.UserJson
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import java.net.URISyntaxException

class SocketRepo(val application: Application) {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://10.128.133.71:3456")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    fun updateUsers() {
        mSocket.emit(application.getString(R.string.update_users))
    }

    fun createUser(userId: Int, username: String, password: String) {
        mSocket.emit(application.getString(R.string.create_user), userId, username, password)
    }

    fun connectUser(userId: Int, username: String) {
        mSocket.emit(application.getString(R.string.connect_user), userId, username)
    }

    fun getAllUsers() {
        mSocket.emit(application.getString(R.string.get_all_users))
    }

    fun createReminder(reminderEntity: ReminderEntity) {
        mSocket.emit(application.getString(R.string.create_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun editReminder(reminderEntity: ReminderEntity) {
        mSocket.emit(application.getString(R.string.edit_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun deleteReminder(reminderEntity: ReminderEntity) {
        mSocket.emit(application.getString(R.string.delete_reminder),
                reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun logout() {
        mSocket.emit(application.getString(R.string.logout))
    }

    fun getAllReminders(userId: Int) {
        mSocket.emit(application.getString(R.string.get_all_reminders), userId)
    }

    fun changeUsername(oldUsername: String, newUsername: String) {
        mSocket.emit(application.getString(R.string.change_username),
                oldUsername,
                newUsername)
    }

    fun onCreateReminder(remindersRepo: RemindersRepo) {
        mSocket.on(application.getString(R.string.create_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as Int,
                        args[4] as Long,
                        args[5] as Long)
                if (remindersRepo.getReminderByID(args[0] as Int) == null) {
                    remindersRepo.addReminder(rem)
                    NotificationUtils().setNotification(rem.time, application.applicationContext, rem.header, rem.description, rem.id)
                }
            }
        }
    }

    fun onEditReminder(remindersRepo: RemindersRepo) {
        mSocket.on(application.getString(R.string.edit_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as Int,
                        args[4] as Long,
                        args[5] as Long)
                if (remindersRepo.getReminderByID(args[0] as Int) != null) {
                    remindersRepo.editReminder(rem)
                    NotificationUtils().setExistNotification(rem.time, application.applicationContext, rem.header, rem.description, rem.id)
                } else {
                    remindersRepo.addReminder(rem)
                    NotificationUtils().setNotification(rem.time, application.applicationContext, rem.header, rem.description, rem.id)
                }
            }
        }
    }

    fun onDeleteReminder(remindersRepo: RemindersRepo) {
        mSocket.on(application.getString(R.string.delete_reminder)) { args ->
            if (args[0] != null) {
                val rem = ReminderEntity(args[0] as Int,
                        args[1] as String,
                        args[2] as String?,
                        args[3] as Int,
                        args[4] as Long,
                        args[5] as Long)
                remindersRepo.deleteReminder(rem)
                NotificationUtils().deleteNotification(application.applicationContext, rem.id)
            }
        }
    }

    fun onCreateUser(userRepo : UserRepo) {
        mSocket.on(application.getString(R.string.create_user)) { args ->
            if (args[0] != null) {
                val user = UserEntity(args[0] as Int, args[1] as String, args[2] as String)
                userRepo.insertUser(user)
            }
        }
    }

    fun onChangeUsername(userRepo : UserRepo, loggedInUserRepo: LoggedInUserRepo) {
        mSocket.on(application.getString(R.string.change_username)) { args ->
            if (args[0] != null) {
                userRepo.editUsername(args[0] as String, args[1] as String)
                if (loggedInUserRepo.getLoggedInUsername(application).equals(args[0] as String)) {
                    loggedInUserRepo.setLoggedIn(application, loggedInUserRepo.getLoggedInUserId(application), args[1] as String)
                }
            }
        }
    }

    fun onGetAllUsers(userRepo: UserRepo) {
        mSocket.on(application.getString(R.string.get_all_users)) { args ->
            if (args[0] != null) {
                val data = args[0] as JSONArray
                val moshi: Moshi = Moshi.Builder().build()
                val listMyData = Types.newParameterizedType(List::class.java, UserJson::class.java)
                val jsonAdapter = moshi.adapter<List<UserJson>>(listMyData)
                val usersList = jsonAdapter.fromJson(data.toString())
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
        mSocket.on(application.getString(R.string.get_all_reminders)) { args ->
            if (args[0] != null) {
                remindersRepo.deleteAllReminders()
                val reminders = args[0] as JSONArray
                val moshi: Moshi = Moshi.Builder().build()
                val listMyData = Types.newParameterizedType(List::class.java, ReminderJson::class.java)
                val jsonAdapter = moshi.adapter<List<ReminderJson>>(listMyData)
                val remindersList = jsonAdapter.fromJson(reminders.toString())
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

    fun jsonToRemEntity(reminderJson: ReminderJson): ReminderEntity {
        return ReminderEntity(reminderJson.id,
                reminderJson.header,
                reminderJson.description,
                reminderJson.user,
                reminderJson.time,
                reminderJson.createdAt)
    }

    fun jsonToUserEntity(userJson: UserJson): UserEntity {
        return UserEntity(userJson.userId,
                userJson.username,
                userJson.password)
    }
}