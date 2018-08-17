package com.example.chris.minigames;

import android.app.Application;

import com.firebase.client.Firebase;

public class Database extends Application {

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);

        super.onCreate();
    }
}
