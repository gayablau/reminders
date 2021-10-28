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
import com.example.androidgaya.util.NotificationUtils
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

    override fun deleteReminderFromDB(reminderEntity: ReminderEntity) {
          remindersDao.deleteReminder(reminderEntity)
    }

    override fun getRemindersFromDB(userId: String): LiveData<List<ReminderEntity>?> {
        return remindersDao.getRemindersByUserId(userId)
    }

    override fun getRemindersByUsernameList(userId: String): List<ReminderEntity> {
        return remindersDao.getRemindersByUserIdList(userId)
    }

    override fun addReminderToDB(reminderEntity: ReminderEntity) {
        remindersDao.addReminder(reminderEntity)
    }

    override fun editReminderFromDB(reminderEntity: ReminderEntity) {
         remindersDao.updateReminder(reminderEntity)
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

    override suspend fun createReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.create_reminder), reminderEntityAdapter.toJson(reminderEntity))
        onCreateReminder(context, reminderEntity)
    }

    override suspend fun onCreateReminder(context: Context, reminderEntity: ReminderEntity) {
        addReminderToDB(reminderEntity)
        NotificationUtils().setNotification(reminderEntity.time,
                context,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.id)
    }

    override suspend fun onCreateReminders(context: Context, reminderEntity: ReminderEntity) {
        addReminderToDB(reminderEntity)
        NotificationUtils().setExistNotification(reminderEntity.time,
                context,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.id)
    }

    override suspend fun editReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.edit_reminder), reminderEntityAdapter.toJson(reminderEntity))
        onEditReminder(context, reminderEntity)
    }

    override suspend fun onEditReminder(context: Context, reminderEntity: ReminderEntity) {
        editReminderFromDB(reminderEntity)
        NotificationUtils().setExistNotification(reminderEntity.time,
                context,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.id)
    }

    override suspend fun deleteReminder(context: Context, reminderEntity: ReminderEntity) {
        socketDao.emit(context.getString(R.string.delete_reminder), reminderEntityAdapter.toJson(reminderEntity))
        onDeleteReminder(context, reminderEntity)
    }

    override suspend fun onDeleteReminder(context: Context, reminderEntity: ReminderEntity) {
        deleteReminderFromDB(reminderEntity)
        socketDao.emit(context.getString(R.string.delete_reminder), reminderEntityAdapter.toJson(reminderEntity))
        NotificationUtils().deleteNotification(context,
                reminderEntity.id)
    }

    override fun logout(context: Context, userId: String) {
        NotificationUtils().cancelAll(context, getMyRemindersIds(userId))
    }

    private val addUserReminders: (Array<Any>, List<Any>) -> Unit = { dataFromSocket: Array<Any>, dataFromClient: List<Any> ->
        dbCoroutineScope.launch {
            if (dataFromSocket[0].toString() != (dataFromClient[1] as Context).getString(R.string.empty_json)) {
                deleteAllReminders()
                val reminders = dataFromSocket[0] as JSONArray
                val remindersList = jsonRemindersAdapter.fromJson(reminders.toString())
                if (remindersList != null) {
                    for (rem in remindersList) {
                        if (getReminderByID(rem.id) == null) {
                            val remToAdd = functions.jsonToRemEntity(rem)
                            onCreateReminders(application, remToAdd)
                        }
                    }
                }
            }
        }
    }
}