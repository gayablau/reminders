package com.example.androidgaya.util

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.androidgaya.main.ui.MainActivity

class LoginNavigator(private val activity: FragmentActivity) {

    fun toMainActivity() {
        val intent = MainActivity.getIntent(activity)
        changeActivity(intent)
    }

    private fun changeActivity(intent : Intent) {
        activity.finishAffinity()
        activity.startActivity(intent)
    }
}