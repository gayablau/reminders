package com.example.androidgaya.repositories.di

import android.app.Application
import android.content.Context
import com.example.androidgaya.repositories.db.AppDatabase
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.interfaces.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun getUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.getUsersDao()
    }

    @Singleton
    @Provides
    fun getRemindersDao(appDatabase: AppDatabase) : RemindersDao {
        return appDatabase.getRemindersDao()
    }

    @Singleton
    @Provides
    fun getRoomDBInstance(): AppDatabase {
        return AppDatabase.getAppDBInstance(provideAppContext())
    }

    @Singleton
    @Provides
    fun provideAppContext() : Context {
        return application.applicationContext
    }
}