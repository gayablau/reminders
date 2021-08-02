package com.example.androidgaya.repositories.di

import com.example.androidgaya.login.viewmodel.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(loginViewModel: LoginViewModel)
}