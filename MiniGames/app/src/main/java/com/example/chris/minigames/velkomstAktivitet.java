package com.example.chris.minigames;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class velkomstAktivitet extends AppCompatActivity implements View.OnClickListener {


    Button btn_spil;
    Button btn_indstillinger;
    public static final String checkbox_key = "checkbox_globalScore";
    static boolean vendt_rigtigt = true;
    Toast t;

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
                    apply();
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



        int orientation = this.getResources().getConfiguration().orientation;
        vendt_rigtigt = orientation == 1;


    }

    @Override
    public void onClick(View v) {
        System.out.println("Den er "+vendt_rigtigt);
        if (v.getId() == R.id.btn_play && vendt_rigtigt) {
            Intent Act_SpilActivity = new Intent(getApplicationContext(), SpilActivity.class);
            startActivity(Act_SpilActivity);

        } else if (v.getId() == R.id.btn_play && !vendt_rigtigt){
            t = new Toast(this);
            ImageView im = new ImageView(this);
            im.setImageResource(R.drawable.ic_menu_always_landscape_portrait);
            im.setAlpha(180);
            t.setView(im);
            t.setGravity(Gravity.CENTER,0,0);
            t.setDuration(Toast.LENGTH_SHORT);
            t.show();


        } else if (v.getId() == R.id.btn_settings) {

            Intent Act_indstillinger = new Intent(getApplicationContext(), Indstillinger_akt.class);
            startActivity(Act_indstillinger);

        }

    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            vendt_rigtigt = false;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            vendt_rigtigt = true;
        }
    }
*/
    @Override
    protected void onPause() {
        if(t != null){
            t.cancel();
        }
        super.onPause();
    }
}
