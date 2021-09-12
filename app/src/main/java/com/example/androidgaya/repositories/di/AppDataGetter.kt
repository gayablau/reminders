package com.example.androidgaya.repositories.di

import androidx.multidex.MultiDexApplication

class AppDataGetter : MultiDexApplication() {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }
}