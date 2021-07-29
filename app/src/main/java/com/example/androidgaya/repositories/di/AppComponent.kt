package com.example.androidgaya.repositories.di

import com.example.androidgaya.main.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainViewModel: MainViewModel)
}