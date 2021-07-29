package com.example.androidgaya.repositories.di

import android.app.Application
import android.content.Context
import com.example.androidgaya.repositories.db.AppDatebase
import com.example.androidgaya.repositories.interfaces.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun getUserDao(appDatebase: AppDatebase) : UserDao {
        return appDatebase.getUsersDao()
    }

    @Singleton
    @Provides
    fun getRoomDBInstance(): AppDatebase {
        return AppDatebase.getAppDBInstance(provideAppContext())
    }

    @Singleton
    @Provides
    fun provideAppContext() : Context {
        return application.applicationContext
    }
}