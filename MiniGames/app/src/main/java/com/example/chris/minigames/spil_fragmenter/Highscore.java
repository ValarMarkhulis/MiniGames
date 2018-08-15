package com.example.chris.minigames.spil_fragmenter;

public class Highscore {

    String navn = "";
    int score = 0;
    String dato = "";

    public Highscore(String navn, int point, String dato) {
        this.navn = navn;
        score = point;
        this.dato = dato;
    }

    @Override
    public String toString() {
        return navn;
    }

    public int getScore() {
        return score;
    }

    public String getDato() {
        return dato;
    }
}
