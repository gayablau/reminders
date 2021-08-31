package com.example.androidgaya.repositories.di

import com.example.androidgaya.details.viewmodel.DetailsViewModel
import com.example.androidgaya.login.viewmodel.LoginViewModel
import com.example.androidgaya.main.socket.SocketService
import com.example.androidgaya.main.viewmodel.MainViewModel
import com.example.androidgaya.profile.viewmodel.ProfileViewModel
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectLogin(loginViewModel: LoginViewModel)
    fun injectMain(mainViewModel: MainViewModel)
    fun injectProfile(profileViewModel: ProfileViewModel)
    fun injectDetails(detailsViewModel: DetailsViewModel)
    fun injectReminders(remindersViewModel: RemindersViewModel)
    fun injectUserRepo(userRepo: UserRepo)
    fun injectRemindersRepo(remindersRepo: RemindersRepo)
    fun injectLoggedInUserRepo(loggedInUserRepo: LoggedInUserRepo)
    fun injectSocketService(socketService: SocketService)
}