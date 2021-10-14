package com.example.androidgaya.repositories.socket

import android.app.Application
import com.example.androidgaya.R
import com.example.androidgaya.repositories.interfaces.SocketCallback
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.reminder.RemindersRepo
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.types.UserJson
import com.example.androidgaya.repositories.user.LoggedInUserRepo
import com.example.androidgaya.repositories.user.UserRepo
import com.example.androidgaya.util.NotificationUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.net.URISyntaxException

class SocketRepo(val application: Application) {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(application.getString(R.string.socket_uri))
        } catch (e: URISyntaxException) {}
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    fun emit(event: String, vararg data : Any){
        mSocket.emit(event, data)
    }

    fun listen(event: String, listener : Emitter.Listener){
        mSocket.on(event, listener)
    }

    fun removeListener(event: String, listener : Emitter.Listener){
        mSocket.off(event, listener)
    }

    fun ask(event: String, vararg data : Any, listener : Emitter.Listener){
        mSocket.emit(event, data).on(event, listener)
    }
}