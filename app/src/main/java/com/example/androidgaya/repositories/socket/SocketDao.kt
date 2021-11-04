package com.example.androidgaya.repositories.socket

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketDao(private val uri: String) {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(uri)
        } catch (e: URISyntaxException) {
        }
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    fun emit(event: String) {
        mSocket.emit(event)
    }

    fun emit(event: String, data: String) {
        mSocket.emit(event, data)
    }

    fun listen(event: String, listener: Emitter.Listener) {
        mSocket.on(event, listener)
    }

    private fun removeListener(event: String) {
        mSocket.off(event)
    }

    fun listenOnce(eventToEmit: String,
                   eventToListen: String,
                   callback: (callbackData: Array<Any>, dataFromClient: String) -> Unit,
                   data: String) {
        emit(eventToEmit, data)
        listen(eventToListen) {
            removeListener(eventToListen)
            callback(it, data)
        }
    }
}