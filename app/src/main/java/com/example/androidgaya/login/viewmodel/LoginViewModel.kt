package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var userDao: UserDao
    private var allusersList: MutableLiveData<ArrayList<UserEntity>>
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectLogin(this)
        allusersList = MutableLiveData()
        getAllUsers()
    }

    fun getUsersObserver(): MutableLiveData<ArrayList<UserEntity>> {
        return allusersList
    }

    fun getAllUsers() {
        val list = userDao.getAllUsersFromDB()
        allusersList.postValue(list as ArrayList<UserEntity>?)
    }

    fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    fun setUsername(username: String) {
        loggedInUserRepo.setUsername(getApplication(), username)
    }

    fun isUserLoggedIn() : Boolean {
        return loggedInUserRepo.isUserLoggedIn(getApplication())
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getUsername(getApplication())
    }

    fun areDetailsOK(username: String, password: String) : Boolean {
        if (userDao.areDetailsOK(username,password) == null) {return false}
        return true
    }

    fun isUserExists(username: String): Boolean {
        if (userDao.findUserByUsername(username) == null) {return false}
        return true
    }

    fun createUser(username: String, password: String) {
        insertUser(UserEntity(username, password))
    }
}