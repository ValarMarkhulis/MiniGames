package com.example.chris.minigames;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.minigames.spil_fragmenter.game_green_btn_frag;
import com.example.chris.minigames.spil_fragmenter.game_ligning_frag;
import com.example.chris.minigames.spil_fragmenter.highscore_frag;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SpilActivity extends AppCompatActivity implements Runnable {

    TextView text_ur;
    ProgressBar progressBar;
    TextView text_score;
    TextView text_HighscoreListTitle;

    int spilnr;
    ArrayList<Fragment> spilListe;
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);

        Singleton.abboner(this);

        this.getWindow().getDecorView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if(savedInstanceState == null){

        Singleton.point = 1; // Reset score
        text_ur = findViewById(R.id.text_ur);
        progressBar = findViewById(R.id.progressBar);
        text_score = findViewById(R.id.text_score);
        text_HighscoreListTitle = findViewById(R.id.text_HighscoreListTitle);
        text_HighscoreListTitle.setVisibility(View.INVISIBLE);
        progressBar.setMax(30);


        spilListe= new ArrayList<Fragment>();
        spilListe.add(new game_ligning_frag());
        spilListe.add(new game_green_btn_frag());
        spilListe.add(new highscore_frag());
        spilnr = 0;

        skiftspil();
        }


        /*
        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new game_ligning_frag())
                //.addToBackStack(null)
                .commit();

        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new game_green_btn_frag())
                //.addToBackStack(null)
                .commit();
                */
    }

    private void startSpil() {

        int spil_length = 30000;
        spil_length = 5000;

        if(spilListe.size() == 0){
            System.out.println(spilnr+" spil blev spillet og highscore siden vises nu.");
            text_score.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            text_ur.setVisibility(View.INVISIBLE);
            text_HighscoreListTitle.setVisibility(View.VISIBLE);

        }else {
            spilnr++;
            System.out.println("Spil nr. "+spilnr+" er startet!");
            cdt = new CountDownTimer(spil_length, 1000) {

                public void onTick(long millisUntilFinished) {
                    text_ur.setText("" + millisUntilFinished / 1000);
                    progressBar.setProgress((int) millisUntilFinished / 1000);
                    text_score.setText("" + Singleton.point);
                }

                public void onFinish() {
                    text_ur.setText("0");
                    progressBar.setProgress(0);
                    text_score.setText("" + Singleton.point);
                    skiftspil();
                }
            }.start();
        }



    }

    private void skiftspil() {

        if(spilListe.size() > 0){
            getFragmentManager().beginTransaction()
                    .replace(R.id.framelayout, spilListe.get(0))
                    //.addToBackStack(null)
                    .commit();

            spilListe.remove(0);
            startSpil();
        }else{
            System.out.println("Der er ikke flere spil tilbage!");

        }


    }


    @Override
    public void run() {
        text_score.setText("" + Singleton.point);
    }

    @Override
    protected void onDestroy() {
        Singleton.afregister(this);
        cdt.cancel();
        super.onDestroy();
    }
}
