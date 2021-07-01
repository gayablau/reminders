package com.example.androidgaya.util

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.androidgaya.login.ui.LoginActivity
import com.example.androidgaya.main.ui.MainActivity

class LoginNavigator(private val activity: FragmentActivity) {

    fun toMainActivity() {
        changeActivity(MainActivity::class.java)
    }

    fun toLoginActivity() {
        changeActivity(LoginActivity::class.java)
    }

    fun changeActivity(toActivity: Class<*>) {
        val intent = Intent(activity, toActivity)
        activity.finishAffinity()
        activity.startActivity(intent)
    }
}