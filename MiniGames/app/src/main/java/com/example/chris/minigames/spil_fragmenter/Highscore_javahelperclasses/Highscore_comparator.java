package com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Highscore_comparator implements Comparator<Highscore> {

    @Override
    public int compare(Highscore h1, Highscore h2) {
        if(h1.getScore() < h2.getScore()){
            return 1;
        }else if(h1.getScore() == h2.getScore()){
            SimpleDateFormat defaultDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            Date dato1;
            Date dato2;
            try {
                dato1 = defaultDateFormat.parse(h1.getDato());
                dato2 = defaultDateFormat.parse(h2.getDato());
                if(dato1.before(dato2)){
                    System.out.println(dato1+" er før "+dato2);
                    return -1;
                }
                return 1;

            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return 0;

        }else{
                return -1;
        }
    }
}
