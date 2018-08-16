package com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses;

import java.util.Comparator;

public class Highscore_comparator implements Comparator<Highscore> {

    @Override
    public int compare(Highscore h1, Highscore h2) {
        if(h1.getScore() < h2.getScore()){
            return 1;
        }else if(h1.getScore() == h2.getScore()){
            return 0;
        }else{
                return -1;
        }
    }
}
