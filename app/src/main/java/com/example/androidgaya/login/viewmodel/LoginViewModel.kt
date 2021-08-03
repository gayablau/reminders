package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.login.ui.AppDataGetter
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInLoggedInUserRepo
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var userDao: UserDao
    private var allusersList: MutableLiveData<List<UserEntity>>
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var loggedInUserRepo : LoggedInLoggedInUserRepo = LoggedInLoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectLogin(this)
        allusersList = MutableLiveData()
        getAllUsers()
    }

    fun getUsersObserver(): MutableLiveData<List<UserEntity>> {
        return allusersList
    }

    fun getAllUsers() {
        val list = userDao.getAllUsersFromDB()
        allusersList.postValue(list)
    }

    fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    fun setUsername(username: String) {
        loggedInUserRepo.setUsername(getApplication(), username)
        remindersRepo.addUsername(username)
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
        //userRepo.createUser(username, password)
        insertUser(UserEntity(username, password))
    }

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
        loggedInUserRepo.setUsername(getApplication(), username)
    }
}