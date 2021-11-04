package com.example.androidgaya.notifications

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androidgaya.R
import com.example.androidgaya.main.ui.MainActivity
import com.example.androidgaya.repositories.models.ReminderEntity
import java.util.*

object NotificationUtils {

    const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
    const val CHANNEL_NAME = "Sample Notification"

    private lateinit var mNotification: Notification
    var id: Int = 0

    private fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                enableVibration(true)
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.parseColor("#e8334a")
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun notify(context: Context, intent: Intent) {
        createChannel(context)
        var timestamp: Long = 0
        var header = ""
        var description = ""
        intent.extras.let {
            if (it != null) {
                timestamp = it.getLong(context.getString(R.string.timestamp))
                header = it.getString(context.getString(R.string.header_uppercase)).toString()
                description = it.getString(context.getString(R.string.description_uppercase)).toString()
                id = it.getInt(context.getString(R.string.id_uppercase))
            }
        }
        if (timestamp > 0) {
            val notifyIntent = Intent(context, MainActivity::class.java)

            notifyIntent.apply {
                putExtra(context.getString(R.string.header_uppercase), header)
                putExtra(context.getString(R.string.description_uppercase), description)
                putExtra(context.getString(R.string.id_uppercase), id)
                putExtra(context.getString(R.string.notification), true)
            }

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            val pendingIntent = PendingIntent.getActivity(context,
                    id,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            val res = context.resources

            mNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_clock_notify)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_clock_notify))
                    .setAutoCancel(true)
                    .setContentTitle(header)
                    .setContentText(description).build()

            val notificationManager: NotificationManager =
                    context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, mNotification)
        }
    }

    fun setExistNotification(context: Context,
                             reminderEntity: ReminderEntity) {

        if (reminderEntity.time > 0) {

            val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

            alarmIntent.apply {
                putExtra(context.getString(R.string.reason), context.getString(R.string.notification))
                reminderEntity.let {
                    putExtra(context.getString(R.string.timestamp), it.time)
                    putExtra(context.getString(R.string.header_uppercase), it.header)
                    putExtra(context.getString(R.string.description_uppercase), it.description)
                    putExtra(context.getString(R.string.id_uppercase), it.id)
                }
            }

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = reminderEntity.time

            val currentTime = Calendar.getInstance()
            if (currentTime.timeInMillis <= calendar.timeInMillis) {
                val pendingIntent = PendingIntent.getBroadcast(context,
                        reminderEntity.id,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent)
            }
        }
    }

    fun setNotification(context: Context,
                        reminderEntity: ReminderEntity) {

        if (reminderEntity.time > 0) {

            val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

            alarmIntent.apply {
                putExtra(context.getString(R.string.reason), context.getString(R.string.notification))
                reminderEntity.let {
                    putExtra(context.getString(R.string.timestamp), it.time)
                    putExtra(context.getString(R.string.header_uppercase), it.header)
                    putExtra(context.getString(R.string.description_uppercase), it.description)
                    putExtra(context.getString(R.string.id_uppercase), it.id)
                }
            }

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = reminderEntity.time

            val pendingIntent = PendingIntent.getBroadcast(context,
                    reminderEntity.id,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent)
        }
    }

    fun deleteNotification(context: Context, reminderEntity: ReminderEntity) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)

        alarmIntent.putExtra(context.getString(R.string.id_uppercase), reminderEntity.id)

        val pendingIntent = PendingIntent.getBroadcast(context,
                reminderEntity.id,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                0,
                pendingIntent)
    }

    fun cancelAll(context: Context, ids: List<Int>) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context.applicationContext, ReminderReceiver::class.java)
        for (id in ids) {
            val pendingIntent = PendingIntent.getBroadcast(context,
                    id,
                    alarmIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    0,
                    pendingIntent)
        }
    }
}