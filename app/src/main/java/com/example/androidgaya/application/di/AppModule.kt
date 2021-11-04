package com.example.androidgaya.application.di

import android.app.Application
import android.content.Context
import com.example.androidgaya.R
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.db.AppDatabase
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserPayload
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.socket.SocketDao
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.types.UserJson
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.util.Functions
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.reflect.ParameterizedType
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    private var socketDao = SocketDao(application.getString(R.string.socket_uri))
    private val moshi: Moshi = Moshi.Builder().build()
    private val listRemindersType: ParameterizedType = Types.newParameterizedType(List::class.java, ReminderJson::class.java)
    private val listUsersType: ParameterizedType = Types.newParameterizedType(List::class.java, UserJson::class.java)

    init {
        socketDao.setSocket()
        socketDao.establishConnection()
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
    fun provideSocket(): SocketDao {
        return socketDao
    }

    @Singleton
    @Provides
    fun provideJsonRemindersAdapter(): JsonAdapter<List<ReminderJson>> {
        return moshi.adapter(listRemindersType)
    }

    @Singleton
    @Provides
    fun provideJsonUsersAdapter(): JsonAdapter<List<UserJson>> {
        return moshi.adapter(listUsersType)
    }

    @Singleton
    @Provides
    fun provideJsonUserAdapter(): JsonAdapter<UserJson> {
        return moshi.adapter(UserJson::class.java)
    }

    @Singleton
    @Provides
    fun provideJsonReminderAdapter(): JsonAdapter<ReminderJson> {
        return moshi.adapter(ReminderJson::class.java)
    }

    @Singleton
    @Provides
    fun provideUserEntityAdapter(): JsonAdapter<LoggedInUserEntity> {
        return moshi.adapter(LoggedInUserEntity::class.java)
    }

    @Singleton
    @Provides
    fun provideReminderEntityAdapter(): JsonAdapter<ReminderEntity> {
        return moshi.adapter(ReminderEntity::class.java)
    }

    @Singleton
    @Provides
    fun provideUserPayloadAdapter(): JsonAdapter<UserPayload> {
        return moshi.adapter(UserPayload::class.java)
    }

    @Singleton
    @Provides
    fun getLoggedInUserRepo(): LoggedInUserRepo {
        return LoggedInUserRepo(application)
    }

    @Singleton
    @Provides
    fun getRemindersRepo(): RemindersRepo {
        return RemindersRepo(application)
    }
}