package com.example.androidgaya.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidgaya.repositories.di.AppDataGetter
import com.example.androidgaya.repositories.models.LoggedInUserEntity
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.socket.SocketHandler.establishConnection
import com.example.androidgaya.repositories.socket.SocketHandler.getSocket
import com.example.androidgaya.repositories.socket.SocketHandler.setSocket
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import io.socket.client.Socket
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepo : UserRepo = UserRepo(application)
    lateinit var loggedInUserList: LiveData<List<LoggedInUserEntity>?>
    private var loggedInUserRepo : LoggedInUserRepo = LoggedInUserRepo(application)

    @set:Inject
    var mSocket: Socket? = null

    init {
        (application as AppDataGetter).getAppComponent()?.injectLogin(this)
        //allusersList = MutableLiveData()
        //getAllUsers()
        updateLoggedInUser()
        getAllUsers()
    }

/*    fun getUsersObserver(): LiveData<ArrayList<UserEntity>> {
        return allusersList
    }

    fun getAllUsers() {
        val list = userRepo.getAllUsers()
        allusersList.postValue(list as ArrayList<UserEntity>?)
    }*/

    fun insertUser(userEntity: UserEntity) {
        userRepo.insertUser(userEntity)
    }

    fun setUsername(username: String) {
        loggedInUserRepo.setLoggedInUsername(getApplication(), username)
    }

    fun isUserLoggedIn() : Boolean {
        return loggedInUserRepo.isUserLoggedIn(getApplication())
    }

    fun getUsername() : String? {
        return loggedInUserRepo.getLoggedInUsername(getApplication())
    }

    fun areDetailsOK(username: String, password: String) : Boolean {
        return userRepo.areDetailsOK(username, password)
    }

    fun isUserExists(username: String): Boolean {
        mSocket!!.emit("updateUsers")
        return userRepo.isUserExists(username)
    }

    fun createUser(username: String, password: String) {
        insertUser(UserEntity(username, password))
        setUsername(username)
        mSocket!!.emit("createUser", username, password)
    }

    fun connectUser(username: String) {
        setUsername(username)
        mSocket!!.emit("connectUser", username)
    }

    fun getAllUsers() {
        mSocket!!.emit("getAllUsers")
    }

    fun getLoggedInUser() : LiveData<List<LoggedInUserEntity>?> {
        return loggedInUserRepo.getLoggedInUserFromDB()
    }

    fun updateLoggedInUser() {
        loggedInUserList = getLoggedInUser()
    }
}