package com.example.androidgaya.application.di

import com.example.androidgaya.socket.SocketService
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.notifications.NotificationUtils
import com.example.androidgaya.profile.ui.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
/*    fun injectLogin(loginActivity: LoginActivity)
    fun injectMain(mainActivity: MainActivity)*/
    fun injectProfile(profileFragment: ProfileFragment)
/*    fun injectDetails(detailsFragment: DetailsFragment)
    fun injectReminders(remindersFragment: RemindersFragment)
    fun injectUserRepo(userRepo: UserRepo)*/
    fun injectRemindersRepo(remindersRepo: RemindersRepo)
    fun injectLoggedInUserRepo(loggedInUserRepo: LoggedInUserRepo)
    fun injectSocketService(socketService: SocketService)
}