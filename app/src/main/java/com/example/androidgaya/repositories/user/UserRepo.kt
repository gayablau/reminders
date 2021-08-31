package com.example.androidgaya.repositories.user

import android.app.Application
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.dao.UserDao
import com.example.androidgaya.repositories.interfaces.UserInterface
import com.example.androidgaya.repositories.models.UserEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRepo(application: Application) : UserInterface {
    @Inject
    lateinit var userDao: UserDao

    init {
        (application as AppDataGetter).getAppComponent()?.injectUserRepo(this)
    }

    override fun editUsername(oldUsername: String, newUsername: String): Unit = runBlocking {
        launch { userDao.editUsername(oldUsername, newUsername) }
    }

    override fun insertUser(userEntity: UserEntity): Unit = runBlocking {
        launch {
            if(!isUserExists(userEntity.username)) {
                userDao.insertUser(userEntity)
            }
        }
    }

    override fun areDetailsOK(username: String, password: String) : Boolean {
        if (userDao.areDetailsOK(username,password) == null) {return false}
        return true
    }

    override fun isUserExists(username: String): Boolean {
        if (userDao.findUserByUsername(username) == null) {return false}
        return true
    }

    override fun findUserIdByUsername(username: String): Int {
        return userDao.findUserIdByUsername(username)
    }
}