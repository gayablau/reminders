package com.example.androidgaya.repositories.di

import com.example.androidgaya.details.viewmodel.DetailsViewModel
import com.example.androidgaya.login.viewmodel.LoginViewModel
import com.example.androidgaya.main.viewmodel.MainViewModel
import com.example.androidgaya.profile.viewmodel.ProfileViewModel
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectLogin(loginViewModel: LoginViewModel)
    fun injectProfile(profileViewModel: ProfileViewModel)
    fun injectDetails(detailsViewModel: DetailsViewModel)
    fun injectReminders(remindersViewModel: RemindersViewModel)
}