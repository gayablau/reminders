package com.example.androidgaya.repositories.socket

import android.app.Application
import android.util.Log
import com.example.androidgaya.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.net.URISyntaxException

class SocketDao(val application: Application) {

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

    fun emit(event: String, vararg data: Any){
        val jsonData = JSONArray(data.asList())
        mSocket.emit(event, jsonData.toString())
    }

    fun emit(event: String, data: List<Any>){
        val jsonData = JSONArray(data)
        mSocket.emit(event, jsonData.toString())
    }

    fun listen(event: String, listener: Emitter.Listener){
        mSocket.on(event, listener)
    }

    fun removeListener(event: String){
        mSocket.off(event)
    }

    fun listenOnce(eventToEmit: String, eventToListen: String, callback: (callbackData : Array<Any>, userDetails: List<Any>) -> Unit, vararg data: Any) {
        emit(eventToEmit, data.asList())
        listen(eventToListen) {
            removeListener(eventToListen)
            callback(it, data.asList())
        }
    }
}