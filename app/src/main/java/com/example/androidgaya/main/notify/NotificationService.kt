package com.example.androidgaya.main.notify

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androidgaya.R
import com.example.androidgaya.main.ui.MainActivity
import java.util.*

class NotificationService : IntentService("NotificationService") {

    companion object {
        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }

    private lateinit var mNotification: Notification
    var id: Int = 0

    @SuppressLint("NewApi")
    private fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

    override fun onHandleIntent(intent: Intent?) {
        createChannel()
        var timestamp: Long = 0
        var header = ""
        var description = ""

        if (intent != null) {
            if (intent.extras != null) {
                timestamp = intent.extras!!.getLong(getString(R.string.timestamp))
                header = intent.extras!!.getString(getString(R.string.header_uppercase)).toString()
                description = intent.extras!!.getString(getString(R.string.description_uppercase)).toString()
                id = intent.extras!!.getInt(getString(R.string.id_uppercase))
            }
        }
        if (timestamp > 0) {
            val context = this.applicationContext
            val notifyIntent = Intent(this, MainActivity::class.java)

            notifyIntent.putExtra(getString(R.string.header_uppercase), header)
            notifyIntent.putExtra(getString(R.string.description_uppercase), description)
            notifyIntent.putExtra(getString(R.string.id_uppercase), id)
            notifyIntent.putExtra(getString(R.string.notification), true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            val pendingIntent = PendingIntent.getActivity(context,
                    id,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources

            mNotification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.alarm_clock_old)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.alarm_clock_old))
                    .setAutoCancel(true)
                    .setContentTitle(header)
                    .setContentText(description).build()

            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, mNotification)
        }
    }
}