package com.example.chris.minigames;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.Set;

public class Indstillinger_akt extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String navne_key = "spillernavn_key";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.indstillinger);

         final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
         //Sæt summary på "spillernavn"
         findPreference("spillernavn_key").setSummary(""+prefs.getString("spillernavn_key", "Standard Navn"));
         context = getBaseContext();


         Preference button2 = findPreference("reset_highscore");
         button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
             @Override
             public boolean onPreferenceClick(Preference arg0) {

                 //Gemmer de eneste to preferences jeg ønsker at beholde
                 String navn = prefs.getString("spillernavn_key", "Standard Navn");
                 Boolean valg = prefs.getBoolean("checkbox_globalScore",false);

                 // Denne kodestumb sletter alt der er i preference manageren
                 // Da det ville kræve mere kode at slette spillernavnene, datoerne og deres score
                 // enkeltvis.
                 prefs.edit().clear().commit();

                 //Genopretter spillernavnet og checkboxens status
                 prefs.edit().
                         putString("spillernavn_key", navn).
                         putBoolean("checkbox_globalScore",valg).
                         apply();
                 Toast.makeText(context,"Highscore liste er blevet nulstillet", Toast.LENGTH_SHORT).show();
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