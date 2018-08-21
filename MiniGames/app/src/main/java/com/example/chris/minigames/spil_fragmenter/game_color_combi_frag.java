package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.chris.minigames.R;
import com.example.chris.minigames.Singleton;

import java.util.Random;

public class game_color_combi_frag extends Fragment implements SeekBar.OnSeekBarChangeListener {
    ImageView farve_change;
    ImageView farve_guess;
    TextView red_helper_text;
    TextView green_helper_text;
    TextView blue_helper_text;
    int red = 0;
    int green= 0;
    int blue = 0;
    int red_guess = 0;
    int green_guess= 0;
    int blue_guess = 0;
    SeekBar seekBar_red;
    SeekBar seekBar_green;
    SeekBar seekBar_blue;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("game_color_combi", "fragmentet blev vist!");

        // Programmatisk layout
        View tl = inflater.inflate(R.layout.game_color_combi_frag, container, false);


        red_helper_text = tl.findViewById(R.id.text_red_help);
        green_helper_text = tl.findViewById(R.id.text_green_help);
        blue_helper_text = tl.findViewById(R.id.text_blue_help);


        farve_change = tl.findViewById(R.id.farve_change);
        farve_guess = tl.findViewById(R.id.farve_guess);
        farve_change.setBackgroundColor(Color.rgb(0,0,0));

        seekBar_red = tl.findViewById(R.id.seekBar_red);
        seekBar_red.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar_red.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar_red.setOnSeekBarChangeListener(this);


        seekBar_green = tl.findViewById(R.id.seekBar_green);
        seekBar_green.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        seekBar_green.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        seekBar_green.setOnSeekBarChangeListener(this);


        seekBar_blue = tl.findViewById(R.id.seekBar_blue);
        seekBar_blue.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        seekBar_blue.getThumb().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        seekBar_blue.setOnSeekBarChangeListener(this);

        findRandomColor();


        return tl;
    }

    private void findRandomColor() {

        Random r = new Random();
        red_guess = r.nextInt(245)+10;
        green_guess = r.nextInt(245)+10;
        blue_guess = r.nextInt(245)+10;

        farve_change.setBackgroundColor(Color.rgb(0,0,0));
        farve_guess.setBackgroundColor(Color.rgb(red_guess,green_guess, blue_guess));

        seekBar_red.setProgress(0);
        seekBar_green.setProgress(0);
        seekBar_blue.setProgress(0);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId() == R.id.seekBar_red){
            //System.out.println("værdien for rød er: "+progress);
            red = progress;
        } else if(seekBar.getId() == R.id.seekBar_green){
            //System.out.println("værdien for grøn er: "+progress);
            green = progress;
        } else if(seekBar.getId() == R.id.seekBar_blue){
            //System.out.println("værdien for blå er: "+progress);
            blue = progress;
        }
        farve_change.setBackgroundColor(Color.rgb(red, green, blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        red_helper_text.setVisibility(View.INVISIBLE);
        green_helper_text.setVisibility(View.INVISIBLE);
        blue_helper_text.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println(ColourDistance()+" distancen");
        red_helper_text.setVisibility(View.INVISIBLE);
        green_helper_text.setVisibility(View.INVISIBLE);
        blue_helper_text.setVisibility(View.INVISIBLE);
        if(ColourDistance() < 100){
            Singleton.point += 3;
            findRandomColor();
        }else if (ColourDistance() > 100 && ColourDistance() < 400){
            //Forsøg på at hjælpe brugeren med hvilken farve og om den
            // skal justeres op eller ned
            int red_dif = red_guess-red;
            boolean red_dif_negativ = false;
            int green_dif = green_guess-green;
            boolean green_dif_negativ = false;
            int blue_dif = blue_guess-blue;
            boolean blue_dif_negativ = false;


            if(red_dif < 0){
                red_dif = red_dif*-1;
                red_dif_negativ = true;
            }

            if(green_dif < 0){
                green_dif = green_dif*-1;
                green_dif_negativ = true;
            }

            if(blue_dif < 0){
                blue_dif = blue_dif*-1;
                blue_dif_negativ = true;
            }


            if(red_dif > green_dif && red_dif > blue_dif){
                red_helper_text.setVisibility(View.VISIBLE);
                if(red_dif_negativ){
                    red_helper_text.setText("-");
                }else{
                    red_helper_text.setText("+");
                }
            }else if(green_dif > red_dif && green_dif > blue_dif){
                green_helper_text.setVisibility(View.VISIBLE);
                if(green_dif_negativ){
                    green_helper_text.setText("-");
                }else{
                    green_helper_text.setText("+");
                }
            }else if(blue_dif > red_dif && blue_dif > green_dif){
                blue_helper_text.setVisibility(View.VISIBLE);
                if(blue_dif_negativ){
                    blue_helper_text.setText("-");
                }else{
                    blue_helper_text.setText("+");
                }
            }
        }
    }

    /*Fundet via https://stackoverflow.com/questions/2103368/color-logic-algorithm */
    public double ColourDistance()
    {
        //Måler distancen/forskellen mellem rød, grøn og blå kannalen.
        double rmean = ( red_guess + red )/2;
        int r = red_guess - red;
        int g = green_guess - green;
        int b = blue_guess - blue;
        double weightR = 2 + rmean/256;
        double weightG = 4.0;
        double weightB = 2 + (255-rmean)/256;
        return Math.sqrt(weightR*r*r + weightG*g*g + weightB*b*b);
    }
}
