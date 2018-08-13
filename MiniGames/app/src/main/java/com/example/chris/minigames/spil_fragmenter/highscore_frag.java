package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.chris.minigames.R;
import com.example.chris.minigames.SpilActivity;

public class highscore_frag extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Velkomst_frag", "fragmentet blev vist!");

        // Programmatisk layout
        View tl = inflater.inflate(R.layout.highscore_frag, container, false);

        Button btn_til_menu = tl.findViewById(R.id.btn_ToMenu_highscore);
        btn_til_menu.setOnClickListener(this);

        Button btn_replay_highscore = tl.findViewById(R.id.btn_replay_highscore);
        btn_replay_highscore.setOnClickListener(this);




        String[] lande = { "Det her er et meget langt navn okay?", "Norge","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?","Det her er et meget langt navn okay?"};
        ListView highScoreListen = (ListView) tl.findViewById(R.id.highscore_list);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lande);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.highscore_liste_design, R.id.navn_Highscorelisten, lande);

        highScoreListen.setAdapter(adapter);

        return tl;
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
