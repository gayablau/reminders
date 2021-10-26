package com.example.androidgaya.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidgaya.R
import com.example.androidgaya.details.viewmodel.DetailsViewModel
import com.example.androidgaya.login.viewmodel.LoginViewModel
import com.example.androidgaya.main.viewmodel.MainViewModel
import com.example.androidgaya.profile.viewmodel.ProfileViewModel
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel
import com.example.androidgaya.repositories.socket.SocketDao

class ViewModelFactory(val application: Application, private val socketDao: SocketDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass.canonicalName) {
            application.getString(R.string.login_viewmodel_path) -> LoginViewModel(application, socketDao) as T
            application.getString(R.string.details_viewmodel_path) -> DetailsViewModel(application, socketDao) as T
            application.getString(R.string.main_viewmodel_path) -> MainViewModel(application, socketDao) as T
            application.getString(R.string.profile_viewmodel_path) -> ProfileViewModel(application, socketDao) as T
            else -> RemindersViewModel(application, socketDao) as T
        }
    }
}