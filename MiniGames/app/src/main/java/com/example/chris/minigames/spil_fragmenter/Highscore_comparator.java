package com.example.chris.minigames.spil_fragmenter;

import java.util.Comparator;

public class Highscore_comparator implements Comparator<Highscore> {

    @Override
    public int compare(Highscore h1, Highscore h2) {
        if(h1.getScore() < h2.getScore()){
            return 1;
        }else{
                return -1;
        }
    }
}
