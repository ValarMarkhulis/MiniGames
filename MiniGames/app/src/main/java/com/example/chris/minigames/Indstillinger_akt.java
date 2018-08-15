package com.example.chris.minigames;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;

public class Indstillinger_akt extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String navne_key = "spillernavn_key";

    @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.indstillinger);

         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
         //Sæt summary på "spillernavn"
         findPreference("spillernavn_key").setSummary(""+prefs.getString("spillernavn_key", "Standard Navn"));



         //Todo: Ændre denne knap så den resetter highscoren!
         Preference button2 = findPreference("reset_highscore");
         button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
             @Override
             public boolean onPreferenceClick(Preference arg0) {
                 finish();
                 return true;
             }
         });


         Preference button = findPreference("Go_to_menu");
         if (button != null) {
             button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                 @Override
                 public boolean onPreferenceClick(Preference arg0) {
                     finish();
                     return true;
                 }
             });
         }

     }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(navne_key)){
            System.out.println("Navnet blev ændret");
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}