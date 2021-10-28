package com.example.androidgaya.util

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androidgaya.R
import com.example.androidgaya.main.notify.ReminderReceiver
import com.example.androidgaya.main.ui.MainActivity
import com.example.androidgaya.repositories.models.ReminderEntity
import java.util.*


class NotificationUtils {

    companion object {
        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }

    private lateinit var mNotification: Notification
    var id: Int = 0

    private fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun notify(context: Context, intent: Intent) {
        createChannel(context)
        var timestamp: Long = 0
        var header = ""
        var description = ""

        if (intent != null) {
            if (intent.extras != null) {
                timestamp = intent.extras!!.getLong(context.getString(R.string.timestamp))
                header = intent.extras!!.getString(context.getString(R.string.header_uppercase)).toString()
                description = intent.extras!!.getString(context.getString(R.string.description_uppercase)).toString()
                id = intent.extras!!.getInt(context.getString(R.string.id_uppercase))
            }
        }
        if (timestamp > 0) {
            val notifyIntent = Intent(context, MainActivity::class.java)

            notifyIntent.putExtra(context.getString(R.string.header_uppercase), header)
            notifyIntent.putExtra(context.getString(R.string.description_uppercase), description)
            notifyIntent.putExtra(context.getString(R.string.id_uppercase), id)
            notifyIntent.putExtra(context.getString(R.string.notification), true)

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
                    .setSmallIcon(R.drawable.clock_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.clock_notify2))
                    .setAutoCancel(true)
                    .setContentTitle(header)
                    .setContentText(description).build()

            val notificationManager: NotificationManager =
                    context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, mNotification)


    }}

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

    fun createAll(context: Context, reminders: List<ReminderEntity>) {
        for (rem in reminders) {
            setExistNotification(rem.time, context, rem.header, rem.description, rem.id)
        }
    }
}