package com.example.androidgaya.repositories.di

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.androidgaya.repositories.di.AppComponent
import com.example.androidgaya.repositories.di.AppModule
import com.example.androidgaya.repositories.di.DaggerAppComponent

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