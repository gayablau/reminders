package com.example.androidgaya.main.vm;

import androidx.lifecycle.ViewModel;

import com.example.androidgaya.repositories.reminder.RemindersRepository;

public class MainViewModel2 extends ViewModel {
/*    String username;

    public MainViewModel2(String username) {
        this.username = username;
    }*/

    RemindersRepository repository = RemindersRepository.getInstance();

    public void addUsername(String username) {
        repository.addUsername(username);
    }
}
