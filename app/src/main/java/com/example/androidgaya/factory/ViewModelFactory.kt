package com.example.androidgaya.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidgaya.details.viewmodel.DetailsViewModel
import com.example.androidgaya.login.viewmodel.LoginViewModel
import com.example.androidgaya.main.viewmodel.MainViewModel
import com.example.androidgaya.profile.viewmodel.ProfileViewModel
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel
import com.example.androidgaya.repositories.socket.SocketRepo

class ViewModelFactory(val application: Application, val socketRepo: SocketRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass.canonicalName) {
            "com.example.androidgaya.login.viewmodel.LoginViewModel" -> LoginViewModel(application, socketRepo) as T
            "com.example.androidgaya.details.viewmodel.DetailsViewModel" -> DetailsViewModel(application, socketRepo) as T
            "com.example.androidgaya.main.viewmodel.MainViewModel" -> MainViewModel(application, socketRepo) as T
            "com.example.androidgaya.profile.viewmodel.ProfileViewModel" -> ProfileViewModel(application, socketRepo) as T
            else -> RemindersViewModel(application, socketRepo) as T
        }
    }
}