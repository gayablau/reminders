package com.example.androidgaya.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.androidgaya.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationUtils::class.java).apply {
            putExtra(context.getString(R.string.reason),
                    intent.getStringExtra(context.getString(R.string.reason)))
            putExtra(context.getString(R.string.timestamp),
                    intent.getLongExtra(context.getString(R.string.timestamp), 0))
            putExtra(context.getString(R.string.header_uppercase),
                    intent.getStringExtra(context.getString(R.string.header_uppercase)))
            putExtra(context.getString(R.string.description_uppercase),
                    intent.getStringExtra(context.getString(R.string.description_uppercase)))
            putExtra(context.getString(R.string.id_uppercase),
                    intent.getIntExtra(context.getString(R.string.id_uppercase), 0))
        }
        NotificationUtils.notify(context, service)
    }
}