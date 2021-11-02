package com.example.androidgaya.application

import com.example.androidgaya.socket.SocketService
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.notifications.NotificationUtils
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectRemindersRepo(remindersRepo: RemindersRepo)
    fun injectLoggedInUserRepo(loggedInUserRepo: LoggedInUserRepo)
    fun injectSocketService(socketService: SocketService)
    fun injectNotificationUtils(notificationUtils: NotificationUtils)
}