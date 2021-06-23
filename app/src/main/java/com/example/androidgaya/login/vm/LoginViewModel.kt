package com.example.androidgaya.login.vm

import androidx.lifecycle.ViewModel
import com.example.androidgaya.repositories.user.UserRepo

class LoginViewModel : ViewModel() {
    fun setIsLoggedIn(isLoggedIn : Boolean) {
        UserRepo.setIsLoggedIn(isLoggedIn)
    }

    fun setUsername(username : String) {
        UserRepo.setUsername(username)
    }

    fun isUserLoggedIn() : Boolean {
        return UserRepo.isUserLoggedIn()
    }

    fun getUsername() : String {
        return UserRepo.getUsername()
    }
}