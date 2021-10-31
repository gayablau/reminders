package com.example.androidgaya.application

import android.app.Application
import android.content.Context
import com.example.androidgaya.factory.ViewModelFactory
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.db.AppDatabase
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
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

    private var socketDao = SocketDao(application)
    private val moshi: Moshi = Moshi.Builder().build()
    private val listRemindersType: ParameterizedType = Types.newParameterizedType(List::class.java, ReminderJson::class.java)
    private val listUsersType: ParameterizedType = Types.newParameterizedType(List::class.java, UserJson::class.java)
    private val jsonRemindersAdapter: JsonAdapter<List<ReminderJson>> = moshi.adapter(listRemindersType)
    private val jsonUsersAdapter: JsonAdapter<List<UserJson>> = moshi.adapter(listUsersType)
    private val jsonUserAdapter: JsonAdapter<UserJson> = moshi.adapter(UserJson::class.java)
    private val jsonReminderAdapter: JsonAdapter<ReminderJson> = moshi.adapter(ReminderJson::class.java)
    private val userEntityAdapter: JsonAdapter<LoggedInUserEntity> = moshi.adapter(LoggedInUserEntity::class.java)
    private val reminderEntityAdapter: JsonAdapter<ReminderEntity> = moshi.adapter(ReminderEntity::class.java)

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

    @Provides
    fun provideFactory(): ViewModelFactory {
        return ViewModelFactory(application)
    }

    @Singleton
    @Provides
    fun provideFunctions(): Functions {
        return Functions()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return moshi
    }

    @Singleton
    @Provides
    fun provideJsonRemindersAdapter(): JsonAdapter<List<ReminderJson>> {
        return jsonRemindersAdapter
    }

    @Singleton
    @Provides
    fun provideJsonUsersAdapter(): JsonAdapter<List<UserJson>> {
        return jsonUsersAdapter
    }

    @Singleton
    @Provides
    fun provideJsonUserAdapter(): JsonAdapter<UserJson> {
        return jsonUserAdapter
    }

    @Singleton
    @Provides
    fun provideJsonReminderAdapter(): JsonAdapter<ReminderJson> {
        return jsonReminderAdapter
    }

    @Singleton
    @Provides
    fun provideUserEntityAdapter(): JsonAdapter<LoggedInUserEntity> {
        return userEntityAdapter
    }

    @Singleton
    @Provides
    fun provideReminderEntityAdapter(): JsonAdapter<ReminderEntity> {
        return reminderEntityAdapter
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

    @Singleton
    @Provides
    fun getDBCoroutineScope(): CoroutineScope {
        val dbCoroutineJob = SupervisorJob()
        return CoroutineScope(Dispatchers.IO + dbCoroutineJob)
    }
}