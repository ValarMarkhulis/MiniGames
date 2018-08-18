package com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses;

public class Highscore {

    private String navn;
    private int score;
    private String dato;
    private boolean global_score;


    // Highscore der hentes fra nettet bliver oprettet med denne konstruktør
    public Highscore() {
        global_score = true;
    }

    //Lokale highscores bliver oprettet med denne konstruktør
    public Highscore(String navn, int point, String dato) {
        this.navn = navn;
        score = point;
        this.dato = dato;
        global_score = false;
    }

    @Override
    public String toString() {
        return navn;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {this.score = score;}

    public String getDato() {
        return dato;
    }
    public void setDato(String dato) {this.dato = dato;}

    public String getNavn() { return navn; }
    public void setNavn(String navn) { this.navn = navn;}

    public boolean isGlobal_score() {return global_score;}
    public void setGlobal_score(boolean global_score) {this.global_score = global_score;}

}
