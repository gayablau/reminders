package com.example.androidgaya.repositories.socket

import android.util.Log
import com.example.androidgaya.repositories.models.ReminderEntity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://10.162.148.106:3456")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    fun updateServer(event: String, data: Any) {
        mSocket.emit(event, data);
    }

    fun updateClient(event: String, data: Any, onUpdate: OnUpdate ) {
        mSocket.on(event, Emitter.Listener { args: Array<Any?> ->
            if (args[0] != null) {
            }
        })
    }

    interface OnUpdate {
        fun update(args: Array<Any?>)
    }
}