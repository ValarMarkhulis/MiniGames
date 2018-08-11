package com.example.chris.minigames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IndstillingerActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btn_reset_score;
    Button btn_save_name;
    Button btn_til_menu;
    TextView input_navn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indstillinger);

        // Find knapper og opsæt
        btn_save_name = findViewById(R.id.btn_save_name);
        btn_save_name.setOnClickListener(this);
        btn_reset_score = findViewById(R.id.btn_reset_score);
        btn_reset_score.setOnClickListener(this);
        btn_til_menu = findViewById(R.id.btn_til_menu);
        btn_til_menu.setOnClickListener(this);

        input_navn = findViewById(R.id.input_navn);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_save_name){
            Singleton.str_spillernavn = input_navn.getText().toString();
            Toast.makeText(IndstillingerActivity.this, "Hej "+Singleton.str_spillernavn, Toast.LENGTH_LONG).show();
        }else if(v.getId() == R.id.btn_reset_score){
            Toast.makeText(IndstillingerActivity.this, "Knappen er ikke implementeret endnu", Toast.LENGTH_SHORT).show();

        }else if(v.getId() == R.id.btn_til_menu){
            finish();
        }

    }
}
