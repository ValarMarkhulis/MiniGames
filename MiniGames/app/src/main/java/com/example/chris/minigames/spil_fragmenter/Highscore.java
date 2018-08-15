package com.example.chris.minigames.spil_fragmenter;

public class Highscore {

    String navn = "";
    int score = 0;
    String dato = "";

    public Highscore(String navn, int point) {
        this.navn = navn;
        score = point;

    }

    @Override
    public String toString() {

        return navn;
    }

    public int getScore() {
        return score;
    }
}
