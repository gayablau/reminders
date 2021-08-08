package com.example.androidgaya.repositories.user

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import javax.inject.Inject

class UserRepo(application: Application) {
    @Inject
    lateinit var userDao: UserDao

    init {
        (application as AppDataGetter).getAppComponent()?.injectUserRepo(this)
    }

    fun editUsername(oldUsername: String, newUsername: String) {
        userDao.editUsername(oldUsername, newUsername)
    }

    fun isUsernameExists(username: String) : Boolean {
        if (userDao.findUserByUsername(username) == null) {return false}
        return true
    }

    fun getAllUsers(): List<UserEntity>? {
        return userDao.getAllUsersFromDB()
    }

    fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    fun areDetailsOK(username: String, password: String) : Boolean {
        if (userDao.areDetailsOK(username,password) == null) {return false}
        return true
    }

    fun isUserExists(username: String): Boolean {
        if (userDao.findUserByUsername(username) == null) {return false}
        return true
    }
}