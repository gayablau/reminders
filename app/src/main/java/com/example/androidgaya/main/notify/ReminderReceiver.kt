package com.example.androidgaya.main.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.androidgaya.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationService::class.java)
        service.putExtra(context.getString(R.string.reason), intent.getStringExtra(context.getString(R.string.reason)))
        service.putExtra(context.getString(R.string.timestamp), intent.getLongExtra(context.getString(R.string.timestamp), 0))
        service.putExtra(context.getString(R.string.header_uppercase), intent.getStringExtra(context.getString(R.string.header_uppercase)))
        service.putExtra(context.getString(R.string.description_uppercase), intent.getStringExtra(context.getString(R.string.description_uppercase)))
        service.putExtra(context.getString(R.string.id_uppercase), intent.getIntExtra(context.getString(R.string.id_uppercase), 0))
        context.startService(service)
    }
}