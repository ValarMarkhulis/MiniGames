package com.example.chris.minigames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class velkomstAktivitet extends AppCompatActivity implements View.OnClickListener {


    Button btn_spil;
    Button btn_indstillinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velkomst_aktivitet);

        // Find knapper og opsæt
        btn_spil = findViewById(R.id.btn_spil);
        btn_spil.setOnClickListener(this);
        btn_indstillinger = findViewById(R.id.btn_indstillinger);
        btn_indstillinger.setOnClickListener(this);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Singleton.win_bredde = displaymetrics.widthPixels;
        Singleton.win_hoejde = displaymetrics.heightPixels;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_spil){
            Intent Act_SpilActivity = new Intent(getApplicationContext(),SpilActivity.class);
            startActivity(Act_SpilActivity);

        }else if(v.getId() == R.id.btn_indstillinger){
            Intent Act_indstillinger = new Intent(getApplicationContext(),IndstillingerActivity.class);
            startActivity(Act_indstillinger);

        }

    }
}
