package com.example.androidgaya.repositories.reminder

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidgaya.R
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.interfaces.ReminderInterface
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.util.Functions
import com.example.androidgaya.notifications.NotificationUtils
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

class RemindersRepo(application: Application) : ReminderInterface {
    @Inject
    lateinit var remindersDao: RemindersDao

    @Inject
    lateinit var socketDao: SocketDao

    @Inject
    lateinit var jsonRemindersAdapter: JsonAdapter<List<ReminderJson>>

    @Inject
    lateinit var jsonReminderAdapter: JsonAdapter<ReminderJson>

    @Inject
    lateinit var reminderEntityAdapter: JsonAdapter<ReminderEntity>

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var dbCoroutineScope : CoroutineScope

    init {
        (application as ReminderApplication).getAppComponent()?.injectRemindersRepo(this)
    }

    override fun getRemindersFromDB(userId: String): LiveData<List<ReminderEntity>?> {
        return remindersDao.getRemindersByUserId(userId)
    }

    override fun getReminderByID(id: Int): ReminderEntity? {
        return remindersDao.getReminderByID(id)
    }

    override fun getMyRemindersIds(userId: String): List<Int> {
        return remindersDao.getMyRemindersIds(userId)
    }

    override fun deleteAllReminders() {
        remindersDao.deleteAllReminders()
    }

    override fun getAllReminders(context: Context, userId: String) {
        socketDao.listenOnce(context.getString(R.string.get_all_reminders), context.getString(R.string.on_get_all_reminders), addUserReminders, userId, context)
    }

    override fun createReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.create_reminder), reminderEntityAdapter.toJson(reminderEntity))
    }

    override fun editReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.edit_reminder), reminderEntityAdapter.toJson(reminderEntity))
    }

    override fun deleteReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.delete_reminder), reminderEntityAdapter.toJson(reminderEntity))
    }

    private val addUserReminders: (Array<Any>, List<Any>) -> Unit = { dataFromSocket: Array<Any>, dataFromClient: List<Any> ->
        dbCoroutineScope.launch {
            if (dataFromSocket[0].toString() != (dataFromClient[1] as Context).getString(R.string.empty_json)) {
                val reminders = dataFromSocket[0] as JSONArray
                val remindersList = jsonRemindersAdapter.fromJson(reminders.toString())
                deleteAllReminders()
                if (remindersList != null) {
                    for (rem in remindersList) {
                        val remToAdd = functions.jsonToRemEntity(rem)
                        remindersDao.addReminder(remToAdd)
                        NotificationUtils().setExistNotification(application, remToAdd)
                    }
                }
            }
        }
    }
}