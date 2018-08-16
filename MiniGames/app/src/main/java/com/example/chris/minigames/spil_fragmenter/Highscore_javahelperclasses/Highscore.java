package com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses;

public class Highscore {

    private String navn;
    private int score;
    private String dato;

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
