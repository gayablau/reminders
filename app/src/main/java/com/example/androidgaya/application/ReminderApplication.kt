package com.example.androidgaya.application

import androidx.multidex.MultiDexApplication
import com.example.androidgaya.application.di.AppComponent
import com.example.androidgaya.application.di.AppModule
import com.example.androidgaya.application.di.DaggerAppComponent

class ReminderApplication : MultiDexApplication() {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }
}