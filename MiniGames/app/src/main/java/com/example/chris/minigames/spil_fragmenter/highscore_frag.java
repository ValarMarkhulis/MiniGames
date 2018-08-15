package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class highscore_frag extends Fragment implements View.OnClickListener {
    private static final int MAX_NAME_LENGTH = 15;
    View tl;
    static String strengen = "";

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

        // Listen der skal vise navne og score sorteret via adapteren
        List<String> list_med_navne = new ArrayList<String>();
        List<String> list_med_score = new ArrayList<String>();


        //Henter værdierne der skal bruges
        String nyt_navn = prefs.getString("spillernavn_key", "Standard Navn");
        nyt_navn = nyt_navn.replaceAll("[^\\x20-\\x7E]", ""); // Frasorterer alle karaktere udover dem der er fra 20 til 126

        if(nyt_navn.length() > MAX_NAME_LENGTH || nyt_navn.trim().equals("") || !nyt_navn.matches("^[a-zA-Z0-9 ]*$")){ // Hvis navnet er for langt eller kun indeholder mellemrum
            nyt_navn = "Spillernavn";
        }else{
            nyt_navn = nyt_navn;
        }

        int ny_score = Singleton.point;
        System.out.println(nyt_navn+" fik "+ny_score+" point");

        Set<String> fetch = prefs.getStringSet("navne", null);
        if(fetch == null){
            Set<String> navne = new HashSet<String>();

            navne.add(nyt_navn);

            Log.d("Navnet ", nyt_navn+" er blevet tilføjet til navnelisten");
            prefs.edit().
                    putStringSet("navne",navne).
                    putInt(nyt_navn, ny_score).
                    apply();

            Toast.makeText(getActivity(), nyt_navn+" fik "+ny_score+ "point", Toast.LENGTH_LONG).show();

            //list_med_navne.add(nyt_navn);
            //fetch = prefs.getStringSet("navne", null);
        }else{
            Set<String> navne = new HashSet<String>();
            List<String> list_usorteret = new ArrayList<String>(fetch);

            boolean flag = false;

            for (int i = 0; i < list_usorteret.size(); i++) {
                navne.add(list_usorteret.get(i));
                if(list_usorteret.get(i).matches(nyt_navn)){
                    flag = true;
                }
            }

            if(flag){ //Hvis navn allerede findes på listen
                if(prefs.getInt(nyt_navn, -1) < ny_score){
                    Log.d("Debug:", nyt_navn+"'s score blev overskrevet med "+ny_score);
                    //Overskriv gammel score
                    prefs.edit().
                            putInt(nyt_navn, ny_score).
                            apply();
                    Toast.makeText(getActivity(), nyt_navn+" fik "+ny_score+ "point", Toast.LENGTH_LONG).show();
                }
            }else{
                Log.d("Debug:", nyt_navn+" blev tilføjet til navnelisten");
                navne.add(nyt_navn);
                prefs.edit().
                        putInt(nyt_navn, ny_score).
                        apply();
                Toast.makeText(getActivity(), nyt_navn+" fik "+ny_score+ "point", Toast.LENGTH_LONG).show();
            }



            // Opdater Stringsættet der er usorteret
            prefs.edit().
                    putStringSet("navne",navne).
                    apply();
        }
        //Herfra er Stringsættet med nyt navn og score

        //Nu skal vi hente Stringsættet igen og begynde at opbygge den sorterede liste i list_med_navne

        // Fetch og navnelist er usorteret
        fetch = prefs.getStringSet("navne", null);
        List<String> navnelist = new ArrayList<String>(fetch);


        for (int i = 0; i < navnelist.size(); i++) {
            Log.d("Debug:::", "navn = " + navnelist.get(i));
        }


        System.out.println("Printer navne + nummer");
        for (int i = 0; i < navnelist.size(); i++) {
            int point =prefs.getInt(navnelist.get(i), -1);
            navnelist.set(i,navnelist.get(i)+ " "+ point);
            System.out.println(navnelist.get(i)+"\n");
        }


        //Nu indeholder navnelist "navn score" i dette format
        try{
            int size = navnelist.size();
            for (int i = 0; i < size; i++) {
                int j = findmax(navnelist);
                list_med_navne.add(navnelist.get(j).substring(0,navnelist.get(j).lastIndexOf(" ")));
                navnelist.remove(j);
                System.out.println("jeg kørte"+i);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }




        //og.d("fetching values", "fetch value " + prefs.getInt(nyt_navn,-1));

        //Log.d("fetching values", "fetch value " + prefs.getInt("Christian",-1));
        //Log.d("fetching values", "fetch value " + prefs.getInt("John",-1));
        //Log.d("fetching values", "fetch value " + prefs.getInt("Peter",-1));
       //Log.d("fetching values", "fetch value " + prefs.getInt("Hej",-1));


        String[] lande = { "Det her er et meget langt navn okay?", "Norge","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?"};
        ListView highScoreListen = (ListView) tl.findViewById(R.id.highscore_list);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lande);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.highscore_liste_design, R.id.navn_Highscorelisten, list_med_navne);

        highScoreListen.setAdapter(adapter);
    }

    private int findmax(List<String> navnelist) {
        int max = -99999;
        int index = 0;
        for (int i = 0; i < navnelist.size(); i++) {
            String Streng = navnelist.get(i);

            int værdi = Integer.parseInt(Streng.substring(Streng.lastIndexOf(" ")+1,Streng.length()));
            if( værdi > max){
                max = værdi;
                index = i;
            }
        }
        return index;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ToMenu_highscore){
            // Luk aktiviteten
            getActivity().finish();

        }else if(v.getId() == R.id.btn_replay_highscore){
            // Luk aktiviteten og start en ny aktivitet af samme type
            getActivity().finish();
            Intent myIntent = new Intent(getActivity(), SpilActivity.class);
            getActivity().startActivity(myIntent);

        }

    }
}