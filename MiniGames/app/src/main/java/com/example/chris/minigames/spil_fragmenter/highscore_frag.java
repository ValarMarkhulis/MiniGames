package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.minigames.R;
import com.example.chris.minigames.Singleton;
import com.example.chris.minigames.SpilActivity;
import com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses.Highscore;
import com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses.Highscore_comparator;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.support.v4.content.ContextCompat.getSystemService;
import static com.example.chris.minigames.R.color.black;


public class highscore_frag extends Fragment implements View.OnClickListener {
    private static final int MAX_NAME_LENGTH = 15;
    View tl;
    List<Highscore> liste_med_personer;
    Firebase myFireBaseref;
    Firebase personer;
    private boolean debug = false;
    ArrayList<Integer> top3 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (debug) Log.d("highscore_frag", "fragmentet blev vist!");

        // Programmatisk layout

        tl = inflater.inflate(R.layout.highscore_frag, container, false);

        ProgressBar pbar = (ProgressBar) tl.findViewById(R.id.highscore_frag_progressbar);

        Button btn_til_menu = tl.findViewById(R.id.btn_ToMenu_highscore);
        btn_til_menu.setOnClickListener(this);

        Button btn_replay_highscore = tl.findViewById(R.id.btn_replay_highscore);
        btn_replay_highscore.setOnClickListener(this);

        lav_Highscore(pbar);

        return tl;
    }

    private void lav_Highscore(final ProgressBar pbar) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Holder listen der vises med Adapteren i listviewet
        liste_med_personer = new ArrayList<>();
        Set<String> fetch;

        if (prefs.getBoolean("checkbox_globalScore", false) && haveNetworkConnection()) { // Hvis der er globalScore og internet

            pbar.setVisibility(View.VISIBLE);
            //Opretter en Firebasereference
            myFireBaseref = new Firebase("https://minigames-719df.firebaseio.com/");

            // Laver en reference til en bestemt version "gren" i mit firebase træ
            personer = myFireBaseref.child("v2").child("personer");

            //Opretter en lytter der kun henter data en gang
            personer.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        //Gennemløb de 3 højeste score og lav dem til Highscore objekter
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Highscore p = new Highscore();
                            p.setNavn(ds.getValue(Highscore.class).getNavn());
                            p.setDato(ds.getValue(Highscore.class).getDato());
                            p.setGlobal_score(ds.getValue(Highscore.class).isGlobal_score());
                            int score = ds.getValue(Highscore.class).getScore();
                            p.setScore(score);
                            top3.add(score);


                            //Læg objekterne i listen der sorteres og vises i Adapteren
                            liste_med_personer.add(p);

                        }

                        setHighscoreOgAdapter(true);
                        //Fjern progressbaren
                        pbar.setVisibility(View.INVISIBLE);
                        if (debug) System.out.println("Færdig med at hente firebase data");

                    } catch (Exception ex) {
                        //Bliver kaldt, hvis brugeren gå til menuen eller trykker spil igen
                        //inden, at dataen er blevet hentet ned og lagt i Adapteren
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        } else if (prefs.getBoolean("checkbox_globalScore", false) && !haveNetworkConnection()) {
            //Hvis brugeren ønskede globalscore, men ikke havde internet
            Toast.makeText(getActivity(), "Du har ingen internet forbindelse!", Toast.LENGTH_SHORT).show();
        }

        //Henter værdierne der skal bruges og inputvalider brugerens navn
        String date = new SimpleDateFormat("HH:mm:ss dd-MM-yy").format(new Date());
        System.out.println(date);
        String nyt_navn = prefs.getString("spillernavn_key", "Standard Navn");
        nyt_navn = nyt_navn.replaceAll("[^\\x20-\\x7E]", ""); // Frasorterer alle karaktere udover dem der er fra 20 til 126

        if (nyt_navn.length() > MAX_NAME_LENGTH || nyt_navn.trim().equals("") || !nyt_navn.matches("^[a-zA-Z0-9 ]*$")) { // Hvis navnet er for langt eller kun indeholder mellemrum
            nyt_navn = "Spillernavn";
        } else {
            nyt_navn = nyt_navn;
        }

        int ny_score = Singleton.point;
        if (debug) System.out.println(nyt_navn + " fik " + ny_score + " point");

        fetch = prefs.getStringSet("navne", null);
        //Hvis det er første spil er der intet gemt i Preferencemangeren
        if (fetch == null) {
            Set<String> navne = new HashSet<String>();

            navne.add(nyt_navn);

            if (debug) Log.d("Navnet ", nyt_navn + " er blevet tilføjet til navnelisten");
            prefs.edit().
                    putStringSet("navne", navne).
                    putInt(nyt_navn, ny_score).
                    putString("date_" + nyt_navn, date).
                    apply();


        } else {
            Set<String> navne = new HashSet<>();
            List<String> list_usorteret = new ArrayList<>(fetch);

            boolean flag = false;

            //Lægger navnene fra listen over i et Set
            for (int i = 0; i < list_usorteret.size(); i++) {
                navne.add(list_usorteret.get(i));
                if (list_usorteret.get(i).matches(nyt_navn)) {
                    flag = true;
                }
            }

            if (flag) { //Hvis navn allerede findes på listen
                if (prefs.getInt(nyt_navn, -1) < ny_score) {
                    if (debug) Log.d("Debug:", nyt_navn + "'s score blev overskrevet med " + ny_score);
                    //Overskriv gammel score
                    prefs.edit().
                            putInt(nyt_navn, ny_score).
                            putString("date_" + nyt_navn, date).
                            apply();
                }
            } else {
                if (debug) Log.d("Debug:", nyt_navn + " blev tilføjet til navnelisten");
                navne.add(nyt_navn);
                prefs.edit().
                        putInt(nyt_navn, ny_score).
                        putString("date_" + nyt_navn, date).
                        apply();
            }

            Toast.makeText(getActivity(), nyt_navn + " fik " + ny_score + " point", Toast.LENGTH_SHORT).show();


            // Opdater Stringsættet der er usorteret
            prefs.edit().
                    putStringSet("navne", navne).
                    apply();
        }

        // Fetch og navnelist er usorteret
        fetch = prefs.getStringSet("navne", null);
        List<String> navnelist = new ArrayList<String>(fetch);


            /*
            //Debug: Sæt navne ind i listen
            for(int i = 0;i <33; i++) {
                navnelist.add("Christian 100");
                navnelist.add("ChristianM 1000");
                navnelist.add("Troels 20");
                navnelist.add("John 5");
                navnelist.add("Pia 1");
                navnelist.add("Petra 123");
                navnelist.add("Sudo 420");
                navnelist.add("Huawei 93");
                navnelist.add("Hvad sa? 39");
            }
            */


        if (debug) {
            for (int i = 0; i < navnelist.size(); i++) {
                Log.d("Debug:::", "navn = " + navnelist.get(i));
            }
        }

        // Opsætter Highscore objekter og putter dem i en liste
        for (int i = 0; i < navnelist.size(); i++) {
            int point = prefs.getInt(navnelist.get(i), -1);
            String navn = navnelist.get(i);
            String dato = prefs.getString("date_" + navn, "Fejl!");
            Highscore person = new Highscore(navn, point, dato);
            if (debug) System.out.println(person.toString());
            liste_med_personer.add(person);
        }

        setHighscoreOgAdapter(false);

    }

    private void setHighscoreOgAdapter(boolean Firebase_data) {
        ListView highScoreListen = tl.findViewById(R.id.highscore_list);

        //Sortere navnene udfra scoren med en comparator
        Highscore_comparator sammenligner = new Highscore_comparator();
        Collections.sort(liste_med_personer, sammenligner);


        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.highscore_liste_design, R.id.navn_Highscorelisten, liste_med_personer) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                //Opsætter de 3 forskellige koloner med data
                TextView score = v.findViewById(R.id.score_Highscorelisten);
                score.setText("" + liste_med_personer.get(position).getScore());
                TextView dato = v.findViewById(R.id.dato_Highscorelisten);
                dato.setText(liste_med_personer.get(position).getDato());

                if (liste_med_personer.get(position).isGlobal_score()) {
                    TextView navn = v.findViewById(R.id.navn_Highscorelisten);
                    navn.setTextColor(getResources().getColor(black));
                    score.setTextColor(getResources().getColor(black));
                    dato.setTextColor(getResources().getColor(black));
                }
                return v;
            }
        };

        //Giver listviewet adapteren
        highScoreListen.setAdapter(adapter);

        if (Firebase_data) {
            //Gennemgå top 3 på listen og upload dem til Global Highscorelisten
            try {
                for (int i = 0; i < 3; i++) {
                    Highscore p = liste_med_personer.get(i);
                    System.out.println(p.getScore() + " nr." + i);
                    System.out.println(top3.get(i) + "");
                    if (p.getScore() != top3.get(i)) {

                        switch (i) {
                            case 0:
                                if (!liste_med_personer.get(0).isGlobal_score()) {
                                    liste_med_personer.get(0).setNavn(liste_med_personer.get(0).getNavn() + " [Global]");
                                    liste_med_personer.get(0).setGlobal_score(true);
                                }

                                personer.child("1").setValue(liste_med_personer.get(0));
                            case 1:
                                if (!liste_med_personer.get(1).isGlobal_score()) {
                                    liste_med_personer.get(1).setNavn(liste_med_personer.get(1).getNavn() + " [Global]");
                                    liste_med_personer.get(1).setGlobal_score(true);
                                }

                                personer.child("2").setValue(liste_med_personer.get(1));
                            case 2:
                                if (!liste_med_personer.get(2).isGlobal_score()) {
                                    liste_med_personer.get(2).setNavn(liste_med_personer.get(2).getNavn() + " [Global]");
                                    liste_med_personer.get(2).setGlobal_score(true);
                                }

                                personer.child("3").setValue(liste_med_personer.get(2));
                        }
                        //personer.child("1").setValue(liste_med_personer.get(0));
                        //personer.child("2").setValue(liste_med_personer.get(1));
                        //personer.child("3").setValue(liste_med_personer.get(2));
                        i = 3;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
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


    /* Fra https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}