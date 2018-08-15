package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.minigames.R;
import com.example.chris.minigames.Singleton;
import com.example.chris.minigames.SpilActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class highscore_frag extends Fragment implements View.OnClickListener {
    private static final int MAX_NAME_LENGTH = 15;
    View tl;
    List<Highscore> liste_med_personer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("highscore_frag", "fragmentet blev vist!");

        // Programmatisk layout

        tl = inflater.inflate(R.layout.highscore_frag, container, false);

        Button btn_til_menu = tl.findViewById(R.id.btn_ToMenu_highscore);
        btn_til_menu.setOnClickListener(this);

        Button btn_replay_highscore = tl.findViewById(R.id.btn_replay_highscore);
        btn_replay_highscore.setOnClickListener(this);

        lav_Highscore();


        return tl;
    }

    private void lav_Highscore() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ListView highScoreListen = (ListView) tl.findViewById(R.id.highscore_list);

        //Holder listen der vises med Adapteren i listviewet
        liste_med_personer = new ArrayList<Highscore>();


        //Henter værdierne der skal bruges og inputvalider brugerens navn

        String nyt_navn = prefs.getString("spillernavn_key", "Standard Navn");
        nyt_navn = nyt_navn.replaceAll("[^\\x20-\\x7E]", ""); // Frasorterer alle karaktere udover dem der er fra 20 til 126

        if (nyt_navn.length() > MAX_NAME_LENGTH || nyt_navn.trim().equals("") || !nyt_navn.matches("^[a-zA-Z0-9 ]*$")) { // Hvis navnet er for langt eller kun indeholder mellemrum
            nyt_navn = "Spillernavn";
        } else {
            nyt_navn = nyt_navn;
        }

        int ny_score = Singleton.point;
        System.out.println(nyt_navn + " fik " + ny_score + " point");

        Set<String> fetch = prefs.getStringSet("navne", null);
        if (fetch == null) {
            Set<String> navne = new HashSet<String>();

            navne.add(nyt_navn);

            Log.d("Navnet ", nyt_navn + " er blevet tilføjet til navnelisten");
            prefs.edit().
                    putStringSet("navne", navne).
                    putInt(nyt_navn, ny_score).
                    apply();


        } else {
            Set<String> navne = new HashSet<String>();
            List<String> list_usorteret = new ArrayList<String>(fetch);

            boolean flag = false;

            for (int i = 0; i < list_usorteret.size(); i++) {
                navne.add(list_usorteret.get(i));
                if (list_usorteret.get(i).matches(nyt_navn)) {
                    flag = true;
                }
            }

            if (flag) { //Hvis navn allerede findes på listen
                if (prefs.getInt(nyt_navn, -1) < ny_score) {
                    Log.d("Debug:", nyt_navn + "'s score blev overskrevet med " + ny_score);
                    //Overskriv gammel score
                    prefs.edit().
                            putInt(nyt_navn, ny_score).
                            apply();
                }
            } else {
                Log.d("Debug:", nyt_navn + " blev tilføjet til navnelisten");
                navne.add(nyt_navn);
                prefs.edit().
                        putInt(nyt_navn, ny_score).
                        apply();
            }

            Toast.makeText(getActivity(), nyt_navn + " fik " + ny_score + " point", Toast.LENGTH_LONG).show();


            // Opdater Stringsættet der er usorteret
            prefs.edit().
                    putStringSet("navne", navne).
                    apply();
        }

        // Fetch og navnelist er usorteret
        fetch = prefs.getStringSet("navne", null);
        List<String> navnelist = new ArrayList<String>(fetch);


        for (int i = 0; i < navnelist.size(); i++) {
            Log.d("Debug:::", "navn = " + navnelist.get(i));
        }


        System.out.println("Printer navne + nummer");
        for (int i = 0; i < navnelist.size(); i++) {
            int point = prefs.getInt(navnelist.get(i), -1);
            String navn = navnelist.get(i);
            Highscore person = new Highscore(navn, point);
            System.out.println(person.toString());
            liste_med_personer.add(person);
        }

        //Sortere navnene udfra scoren
        Highscore_comparator sammenligner = new Highscore_comparator();
        Collections.sort(liste_med_personer, sammenligner);


        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.highscore_liste_design, R.id.navn_Highscorelisten, liste_med_personer) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView text1 = v.findViewById(R.id.score_Highscorelisten);
                text1.setText("" + liste_med_personer.get(position).score);

                return v;
            }
        };

        highScoreListen.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ToMenu_highscore) {
            // Luk aktiviteten
            getActivity().finish();

        } else if (v.getId() == R.id.btn_replay_highscore) {
            // Luk aktiviteten og start en ny aktivitet af samme type
            getActivity().finish();
            Intent myIntent = new Intent(getActivity(), SpilActivity.class);
            getActivity().startActivity(myIntent);

        }

    }
}