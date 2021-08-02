package com.example.androidgaya.repositories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUsersDao(): UserDao
    companion object {
        private var dbINSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if(dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder<AppDatabase>(
                        context.applicationContext, AppDatabase::class.java, "RemindersDatebase"
                ).allowMainThreadQueries().build()

            }
            return dbINSTANCE!!
        }
    }
}