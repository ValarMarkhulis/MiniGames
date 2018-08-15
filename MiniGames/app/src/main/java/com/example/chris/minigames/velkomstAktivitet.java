package com.example.chris.minigames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class velkomstAktivitet extends AppCompatActivity implements View.OnClickListener {


    Button btn_spil;
    Button btn_indstillinger;
    public static final String checkbox_key = "checkbox_globalScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velkomst_aktivitet);
        if(savedInstanceState == null){




            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //Sæt std spillernavn
            prefs.
                    edit().
                    putString("spillernavn_key", "Navn").
                    commit();
        }
        // Find knapper og opsæt
        btn_spil = findViewById(R.id.btn_play);
        btn_spil.setOnClickListener(this);
        btn_indstillinger = findViewById(R.id.btn_settings);
        btn_indstillinger.setOnClickListener(this);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Singleton.win_bredde = displaymetrics.widthPixels;
        Singleton.win_hoejde = displaymetrics.heightPixels;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {
            Intent Act_SpilActivity = new Intent(getApplicationContext(), SpilActivity.class);
            startActivity(Act_SpilActivity);

        } else if (v.getId() == R.id.btn_settings) {

            Intent Act_indstillinger = new Intent(getApplicationContext(), Indstillinger_akt.class);
            startActivity(Act_indstillinger);

        }

    }
}
