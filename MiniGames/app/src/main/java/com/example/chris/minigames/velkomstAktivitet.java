package com.example.chris.minigames;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class velkomstAktivitet extends AppCompatActivity implements View.OnClickListener {


    Button btn_spil;
    Button btn_indstillinger;
    Button btn_highscore;
    static boolean vendt_rigtigt = true;
    Toast t;
    FragmentManager fm = getSupportFragmentManager();
    private boolean debug = false;
    private static boolean first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_velkomst_aktivitet);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Sæt standard spillernavn, hvis brugeren ikke har sat et endnu
        if(prefs.getString("spillernavn_key", "fejl").equals("fejl")){
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
        btn_highscore = findViewById(R.id.btn_highscore);
        btn_highscore.setOnClickListener(this);


        // Find skærmstørrelsen og gem den
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Singleton.win_bredde = displaymetrics.widthPixels;
        Singleton.win_hoejde = displaymetrics.heightPixels;

        //Få orienteringen af skærmen
        int orientation = this.getResources().getConfiguration().orientation;
        vendt_rigtigt = orientation == 1;

    }

    @Override
    public void onClick(View v) {

        if(debug) System.out.println("Den er "+vendt_rigtigt);
        if (v.getId() == R.id.btn_play && vendt_rigtigt) {
            Intent Act_SpilActivity = new Intent(getApplicationContext(), SpilActivity.class);
            startActivity(Act_SpilActivity);

            //Hvis skærmen vender forkert
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

        }else if (v.getId() == R.id.btn_highscore) {

            MyDialogFragment dialogFragment = new MyDialogFragment();
            dialogFragment.show(fm, "Sample Fragment");

        }

    }


    @Override
    protected void onPause() {
        // Afbryd toasten, da en nullpointerException ellers kan forekomme ved skærmvending eller
        // toasten bliver vist ind over en anden aktivitet
        if(t != null){
            t.cancel();
        }
        super.onPause();
    }
}
