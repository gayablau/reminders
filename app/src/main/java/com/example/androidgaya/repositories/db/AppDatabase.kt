package com.example.androidgaya.repositories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidgaya.R
import com.example.androidgaya.repositories.dao.LoggedInUserDao
import com.example.androidgaya.repositories.dao.RemindersDao
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity

@Database(entities = [ReminderEntity::class,
    LoggedInUserEntity::class],
        version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRemindersDao(): RemindersDao
    abstract fun getLoggedInUserDao(): LoggedInUserDao

    companion object {
        private var dbINSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if (dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        context.getString(R.string.Reminders_database)
                ).allowMainThreadQueries().build()
            }
            return dbINSTANCE!!
        }
    }
}