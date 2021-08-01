package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.interfaces.UserDao
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.UserRepo
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var userDao: UserDao

    lateinit var allusersList: MutableLiveData<List<UserEntity>>
    private var remindersRepo : RemindersRepo = RemindersRepo.getInstance()
    private var userRepo : UserRepo = UserRepo(application)

    /*init {
        allusersList = MutableLiveData()
        getAllRecords()
    }

    fun getRecordsObserver(): MutableLiveData<List<UserEntity>> {
        return allusersList
    }

    fun getAllRecords() {
        val list = userDao.getAllRecordsFromDB()
        allusersList.postValue(list)
    }

    fun insertRecord(userEntity: UserEntity) {
        userDao.insertRecord(userEntity)
    }
*/

    fun setUsername(username : String) {
        userRepo.setUsername(getApplication(), username)
        remindersRepo.addUsername(username)
    }

    fun isUserLoggedIn() : Boolean {
        return userRepo.isUserLoggedIn(getApplication())
    }

    fun getUsername() : String? {
        return userRepo.getUsername(getApplication())
    }

    fun areDetailesOK(username: String, password: String) : Boolean {
        return userRepo.areDetailsOK(username, password)
    }

    fun isUserExists(username: String): Boolean {
        return userRepo.isUserExists(username)
    }

    fun createUser(username: String, password: String) {
        userRepo.createUser(username, password)
    }

    fun addUsername(username: String) {
        remindersRepo.addUsername(username)
        userRepo.setUsername(getApplication(), username)
    }
}