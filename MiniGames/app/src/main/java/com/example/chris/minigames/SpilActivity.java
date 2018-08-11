package com.example.chris.minigames;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.minigames.spil_fragmenter.game_green_btn_frag;
import com.example.chris.minigames.spil_fragmenter.game_ligning_frag;

import org.w3c.dom.Text;

public class SpilActivity extends AppCompatActivity {

    TextView text_ur;
    ProgressBar progressBar;
    TextView text_score;

    int spilnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);

        text_ur = findViewById(R.id.text_ur);
        progressBar = findViewById(R.id.progressBar);
        text_score = findViewById(R.id.text_score);
        progressBar.setMax(30);


        startNytSpil();
        /*
        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new game_ligning_frag())
                //.addToBackStack(null)
                .commit();
        */
        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new game_green_btn_frag())
                //.addToBackStack(null)
                .commit();
    }

    private void startNytSpil() {
        System.out.println("Ny spil er startet!");
        spilnr++;
        int spil_length = 30000;
        //spil_length = 5000;


        new CountDownTimer(spil_length, 1000) {

            public void onTick(long millisUntilFinished) {
                text_ur.setText(""+millisUntilFinished / 1000);
                progressBar.setProgress( (int)millisUntilFinished/1000);
                text_score.setText(""+Singleton.point);
            }

            public void onFinish() {
                text_ur.setText("0");
                progressBar.setProgress(0);
                text_score.setText(""+Singleton.point);
            }
        }.start();




    }


}
