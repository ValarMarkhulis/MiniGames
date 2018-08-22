package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chris.minigames.R;
import com.example.chris.minigames.Singleton;

import java.util.Random;

public class game_green_btn_frag extends Fragment implements View.OnClickListener {
    ImageView knap;
    private boolean debug = false;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("game_green_btn", "fragmentet blev vist!");

        // Programmatisk layout
        View tl = inflater.inflate(R.layout.game_green_btn_frag, container, false);

        knap = tl.findViewById(R.id.pic_GreenBtn);
        knap.setOnClickListener(this);

        View screen = tl.findViewById(R.id.layout_GreenBtn);
        screen.setOnClickListener(this);

        return tl;
    }

        @Override
    public void onClick(View v) {

        if(v == knap){
            Singleton.point++;
        }else{
            Singleton.point--;
        }
        Singleton.opdater();
        Random r = new Random();

        //Sæt ny position af knap
        knap.setX(r.nextInt(Singleton.win_bredde)/2);
        knap.setY((r.nextInt(Singleton.win_hoejde))/2+100);

    }

}
