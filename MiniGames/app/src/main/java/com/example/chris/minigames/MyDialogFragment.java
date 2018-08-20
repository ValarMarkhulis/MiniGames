package com.example.chris.minigames;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses.Highscore;
import com.example.chris.minigames.spil_fragmenter.Highscore_javahelperclasses.Highscore_comparator;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.chris.minigames.R.color.black;

public class MyDialogFragment extends DialogFragment{
    private static final int MAX_NAME_LENGTH = 15;
    List<Highscore> liste_med_personer;
    Firebase myFireBaseref;
    Firebase personer;
    View rootView;
    boolean debug = true;
    ArrayList<Integer> top3 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);

        rootView = inflater.inflate(R.layout.highscoretomenu_frag, container, false);
        ProgressBar pbar = (ProgressBar) rootView.findViewById(R.id.highscore_frag_progressbar);

        lav_Highscore(pbar);
        return rootView;
    }

    private void lav_Highscore(final ProgressBar pbar) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ListView highScoreListen = rootView.findViewById(R.id.highscore_list);

        //Holder listen der vises med Adapteren i listviewet
        liste_med_personer = new ArrayList<Highscore>();
        Set<String> fetch;

        if(prefs.getBoolean("checkbox_globalScore",false) && haveNetworkConnection()) { // Hvis der er globalScore og internet

            pbar.setVisibility(View.VISIBLE);
            //Opret Highscore objekter, hvis det er slået til i indstillinger
            myFireBaseref = new Firebase("https://minigames-719df.firebaseio.com/");

            // Hent Firebase data som
            personer = myFireBaseref.child("v2").child("personer");


            personer.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        ArrayList<Highscore> personer_firebase = new ArrayList<Highscore>();


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
                        if(debug) System.out.println("Færdig med at hente firebase data");

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


        }else if(prefs.getBoolean("checkbox_globalScore",false) && !haveNetworkConnection()){
            //Hvis brugeren ønskede globalscore, men ikke havde internet
            Toast.makeText(getActivity(), "Du har ingen internet forbindelse!", Toast.LENGTH_SHORT).show();
        }


        // Fetch og navnelist er usorteret
        fetch = prefs.getStringSet("navne", null);
        if(fetch == null ){
            if(!prefs.getBoolean("checkbox_globalScore",false)){
                Toast.makeText(getActivity(), "Der er ingen lokal highscore!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
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


        for (int i = 0; i < navnelist.size(); i++) {
            int point = prefs.getInt(navnelist.get(i), -1);
            String navn = navnelist.get(i);
            String dato =prefs.getString("date_"+navn,"Fejl!");
            Highscore person = new Highscore(navn, point, dato);
            if(debug)System.out.println(person.toString());
            liste_med_personer.add(person);
        }

        setHighscoreOgAdapter(false);

    }

    private void setHighscoreOgAdapter(boolean Firebase_data) {
        ListView highScoreListen = rootView.findViewById(R.id.highscore_list);
        //Sortere navnene udfra scoren
        Highscore_comparator sammenligner = new Highscore_comparator();
        Collections.sort(liste_med_personer, sammenligner);



        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.highscore_liste_design, R.id.navn_Highscorelisten, liste_med_personer) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView score = v.findViewById(R.id.score_Highscorelisten);
                score.setText("" + liste_med_personer.get(position).getScore());
                TextView dato  = v.findViewById(R.id.dato_Highscorelisten);
                dato.setText(liste_med_personer.get(position).getDato());

                if(liste_med_personer.get(position).isGlobal_score()){
                    TextView navn = v.findViewById(R.id.navn_Highscorelisten);
                    navn.setTextColor(getResources().getColor(black));
                    score.setTextColor(getResources().getColor(black));
                    dato.setTextColor(getResources().getColor(black));
                }
                return v;
            }
        };

        highScoreListen.setAdapter(adapter);

        if(Firebase_data){
            //Gennemgå top 3 på listen og upload dem til Global Highscorelisten
            try{
                for (int i = 0; i<3;i++){
                    Highscore p = liste_med_personer.get(i);
                    System.out.println(p.getScore()+" nr."+i);
                    System.out.println(top3.get(i)+"");
                    if(p.getScore() != top3.get(i)){

                        switch (i){
                            case 0:
                                if(!liste_med_personer.get(0).isGlobal_score()){
                                    liste_med_personer.get(0).setNavn(liste_med_personer.get(0).getNavn()+" [Global]");
                                    liste_med_personer.get(0).setGlobal_score(true);
                                }

                                personer.child("1").setValue(liste_med_personer.get(0));
                            case 1:
                                if(!liste_med_personer.get(1).isGlobal_score()){
                                    liste_med_personer.get(1).setNavn(liste_med_personer.get(1).getNavn()+" [Global]");
                                    liste_med_personer.get(1).setGlobal_score(true);
                                }

                                personer.child("2").setValue(liste_med_personer.get(1));
                            case 2:
                                if(!liste_med_personer.get(2).isGlobal_score())
                                {
                                    liste_med_personer.get(2).setNavn(liste_med_personer.get(2).getNavn()+" [Global]");
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

            }catch (Exception ex){
                ex.printStackTrace();
            }

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
