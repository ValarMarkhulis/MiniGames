package com.example.chris.minigames;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;

public class Singleton extends Application {

    public static String str_spillernavn = "Name";
    public static int point = 0;
    public static int win_bredde = 0;
    public static int win_hoejde = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Singletonklass's onCreate() blev kaldt!");

    }


}
