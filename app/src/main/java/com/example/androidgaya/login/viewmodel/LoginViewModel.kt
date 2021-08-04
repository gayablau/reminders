package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepo : UserRepo = UserRepo(application)
    private var allusersList: MutableLiveData<ArrayList<UserEntity>>
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)

    init {
        (application as AppDataGetter).getAppComponent()?.injectLogin(this)
        allusersList = MutableLiveData()
        getAllUsers()
    }

    fun getUsersObserver(): MutableLiveData<ArrayList<UserEntity>> {
        return allusersList
    }

    fun getAllUsers() {
        val list = userRepo.getAllUsers()
        allusersList.postValue(list as ArrayList<UserEntity>?)
    }

    fun insertUser(userEntity: UserEntity) {
        userRepo.insertUser(userEntity)
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
        return userRepo.areDetailsOK(username, password)
    }

    fun isUserExists(username: String): Boolean {
        return userRepo.isUserExists(username)
    }

    fun createUser(username: String, password: String) {
        insertUser(UserEntity(username, password))
    }
}