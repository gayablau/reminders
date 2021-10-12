package com.example.androidgaya.repositories.di

import android.app.Application
import android.content.Context
import com.example.androidgaya.factory.ViewModelFactory
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.dao.UserDao
import com.example.androidgaya.repositories.db.AppDatabase
import com.example.androidgaya.repositories.socket.SocketRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    var socketRepo = SocketRepo(application)

    init {
        socketRepo.setSocket()
        socketRepo.establishConnection()
    }

    @Singleton
    @Provides
    fun getUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.getUsersDao()
    }

    @Singleton
    @Provides
    fun getRemindersDao(appDatabase: AppDatabase): RemindersDao {
        return appDatabase.getRemindersDao()
    }

    @Singleton
    @Provides
    fun getLoggedInUserDao(appDatabase: AppDatabase): LoggedInUserDao {
        return appDatabase.getLoggedInUserDao()
    }

    @Singleton
    @Provides
    fun getRoomDBInstance(): AppDatabase {
        return AppDatabase.getAppDBInstance(provideAppContext())
    }

    @Singleton
    @Provides
    fun provideAppContext(): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideSocket(): SocketRepo {
        return socketRepo
    }

    @Provides
    fun provideFactory(): ViewModelFactory {
        return ViewModelFactory(application, provideSocket())
    }
}