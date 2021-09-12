package com.example.androidgaya.util

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.androidgaya.R
import com.example.androidgaya.main.notify.ReminderReceiver
import com.example.androidgaya.repositories.models.ReminderEntity
import java.util.*

class NotificationUtils {
    fun setExistNotification(timeInMilliSeconds: Long,
                             context: Context,
                             header: String,
                             description: String,
                             id: Int) {

        if (timeInMilliSeconds > 0) {

            val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

            alarmIntent.putExtra(context.getString(R.string.reason), context.getString(R.string.notification))
            alarmIntent.putExtra(context.getString(R.string.timestamp), timeInMilliSeconds)
            alarmIntent.putExtra(context.getString(R.string.header_uppercase), header)
            alarmIntent.putExtra(context.getString(R.string.description_uppercase), description)
            alarmIntent.putExtra(context.getString(R.string.id_uppercase), id)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds
            calendar.add(Calendar.MONTH, -1)

            val currentTime = Calendar.getInstance()
            if (currentTime.timeInMillis <= calendar.timeInMillis) {
                val pendingIntent = PendingIntent.getBroadcast(context,
                        id,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent)
            }
        }
    }

    fun setNotification(timeInMilliSeconds: Long,
                        context: Context,
                        header: String,
                        description: String,
                        id: Int) {

        if (timeInMilliSeconds > 0) {

            val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

            alarmIntent.putExtra(context.getString(R.string.reason), context.getString(R.string.notification))
            alarmIntent.putExtra(context.getString(R.string.timestamp), timeInMilliSeconds)
            alarmIntent.putExtra(context.getString(R.string.header_uppercase), header)
            alarmIntent.putExtra(context.getString(R.string.description_uppercase), description)
            alarmIntent.putExtra(context.getString(R.string.id_uppercase), id)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds
            calendar.add(Calendar.MONTH, -1)

            val pendingIntent = PendingIntent.getBroadcast(context,
                    id,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent)
        }
    }

    fun deleteNotification(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

        alarmIntent.putExtra(context.getString(R.string.id_uppercase), id)

        val pendingIntent = PendingIntent.getBroadcast(context,
                id,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                0,
                pendingIntent)
    }

    fun cancelAll(activity: Activity, ids: List<Int>) {
        val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(activity.applicationContext, ReminderReceiver::class.java)
        for (id in ids) {
            val pendingIntent = PendingIntent.getBroadcast(activity,
                    id,
                    alarmIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    0,
                    pendingIntent)
        }
    }

    fun createAll(activity: Activity, reminders: List<ReminderEntity>) {
        for (rem in reminders) {
            setExistNotification(rem.time, activity, rem.header, rem.description, rem.id)
        }
    }
}