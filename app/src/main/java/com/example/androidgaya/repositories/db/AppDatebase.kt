package com.example.androidgaya.repositories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatebase : RoomDatabase() {
    abstract fun getUsersDao(): UserDao
    companion object {
        private var dbINSTANCE: AppDatebase? = null

        fun getAppDBInstance(context: Context): AppDatebase {
            if(dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder<AppDatebase>(
                        context.applicationContext, AppDatebase::class.java, "RemindersDatebase"
                ).allowMainThreadQueries().build()

            }
            return dbINSTANCE!!
        }
    }
}