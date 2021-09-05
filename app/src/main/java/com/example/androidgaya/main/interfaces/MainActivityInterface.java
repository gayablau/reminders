package com.example.androidgaya.main.interfaces;

import com.example.androidgaya.util.MainNavigator;

import io.socket.client.Socket;

public interface MainActivityInterface {
    MainNavigator getNavigator();
    void changeToolbar(String title, Boolean back);
}
