package com.example.androidgaya.repositories.di

import com.example.androidgaya.details.ui.DetailsFragment
import com.example.androidgaya.login.ui.LoginActivity
import com.example.androidgaya.main.socket.SocketService
import com.example.androidgaya.main.ui.MainActivity
import com.example.androidgaya.profile.ui.ProfileFragment
import com.example.androidgaya.reminders.ui.RemindersFragment
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectLogin(loginActivity: LoginActivity)
    fun injectMain(mainActivity: MainActivity)
    fun injectProfile(profileFragment: ProfileFragment)
    fun injectDetails(detailsFragment: DetailsFragment)
    fun injectReminders(remindersFragment: RemindersFragment)
    fun injectUserRepo(userRepo: UserRepo)
    fun injectRemindersRepo(remindersRepo: RemindersRepo)
    fun injectLoggedInUserRepo(loggedInUserRepo: LoggedInUserRepo)
    fun injectSocketService(socketService: SocketService)
}