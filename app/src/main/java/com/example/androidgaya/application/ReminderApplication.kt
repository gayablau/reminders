package com.example.androidgaya.application

import androidx.multidex.MultiDexApplication

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