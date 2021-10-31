package com.example.androidgaya.application

import com.example.androidgaya.details.ui.DetailsFragment
import com.example.androidgaya.login.ui.LoginActivity
import com.example.androidgaya.main.socket.SocketService
import com.example.androidgaya.main.ui.MainActivity
import com.example.androidgaya.profile.ui.ProfileFragment
import com.example.androidgaya.reminders.ui.RemindersFragment
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.util.NotificationUtils
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
    fun injectRemindersRepo(remindersRepo: RemindersRepo)
    fun injectLoggedInUserRepo(loggedInUserRepo: LoggedInUserRepo)
    fun injectSocketService(socketService: SocketService)
    fun injectNotificationUtils(notificationUtils: NotificationUtils)
}