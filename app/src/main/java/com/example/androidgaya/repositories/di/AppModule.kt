package com.example.androidgaya.repositories.di

import android.app.Application
import android.content.Context
import com.example.androidgaya.repositories.db.AppDatabase
import com.example.androidgaya.repositories.interfaces.LoggedInUserDao
import com.example.androidgaya.repositories.interfaces.RemindersDao
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.socket.SocketHandler
import dagger.Module
import dagger.Provides
import io.socket.client.Socket
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
    fun getLoggedInUserDao(appDatabase: AppDatabase) : LoggedInUserDao {
        return appDatabase.getLoggedInUserDao()
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

    @Singleton
    @Provides
    fun provideSocket() : Socket {
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        return SocketHandler.mSocket
    }
}