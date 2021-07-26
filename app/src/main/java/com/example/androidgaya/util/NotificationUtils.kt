package com.example.androidgaya.util

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Intent
import com.example.androidgaya.R
import com.example.androidgaya.main.notify.ReminderReceiver
import java.util.*

class NotificationUtils {
    fun setNotification(timeInMilliSeconds: Long, activity: Activity, header: String, description: String) {

        //------------  alarm settings start  -----------------//

        if (timeInMilliSeconds > 0) {


            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, ReminderReceiver::class.java) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra(activity.getString(R.string.reason), activity.getString(R.string.notification))
            alarmIntent.putExtra(activity.getString(R.string.timestamp), timeInMilliSeconds)
            alarmIntent.putExtra(activity.getString(R.string.header), header)
            alarmIntent.putExtra(activity.getString(R.string.description), description)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds
            calendar.add(Calendar.MONTH, -1)
            if (android.os.Build.VERSION.SDK_INT == 19) {
                calendar.add(Calendar.MINUTE, -1)
            }

            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        }

        //------------ end of alarm settings  -----------------//


    }
}