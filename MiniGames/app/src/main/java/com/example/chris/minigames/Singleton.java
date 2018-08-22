package com.example.chris.minigames;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class Singleton extends Application {

    public static int point = 0;
    public static int win_bredde = 0;
    public static int win_hoejde = 0;
    private Boolean debug = false;

    public static List<Runnable> liste_run = new ArrayList<Runnable>();

    @Override
    public void onCreate() {
        super.onCreate();
        if(debug) System.out.println("Singletonklass's onCreate() blev kaldt!");

    }


    public static void abboner(Runnable r){
        liste_run.add(r);
    }

    public static void afregister(Runnable r){
        liste_run.remove(r);
    }

    public static void opdater(){
        for(Runnable r : liste_run){
            r.run();
        }
    }

}
