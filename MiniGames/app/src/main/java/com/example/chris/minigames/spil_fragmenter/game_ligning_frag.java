package com.example.chris.minigames.spil_fragmenter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.minigames.R;
import com.example.chris.minigames.Singleton;

import java.util.Random;


public class game_ligning_frag extends Fragment  implements View.OnClickListener {

    TextView ligningsfelt;
    Button svar1;
    Button svar2;
    Button svar3;
    Button svar4;
    int antalKnapper = 4;
    public ImageView flueben;
    int hvilkenKnapKorrekt;
    TextView antalRigtigeText;
    TextView antalForkerteText;
    private Toast mToast;
    private boolean debug = false;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (debug) Log.d("game_ligning_frag", "fragmentet blev vist!");

            // Programmatisk layout
            View tl = inflater.inflate(R.layout.game_ligning_frag, container, false);

            ligningsfelt = tl.findViewById(R.id.equation);
            svar1 = tl.findViewById(R.id.answerbutton1);
            svar2 = tl.findViewById(R.id.answerbutton2);
            svar3 = tl.findViewById(R.id.answerbutton3);
            svar4 = tl.findViewById(R.id.answerbutton4);
            flueben = tl.findViewById(R.id.pic_equation);
            flueben.setVisibility(View.INVISIBLE);

            antalRigtigeText = tl.findViewById(R.id.text_NumberCorrect);
            antalForkerteText = tl.findViewById(R.id.text_NumberWrong);

            if(savedInstanceState != null){
                antalRigtigeText.setText(""+savedInstanceState.getString("antalRigtige"));
                antalForkerteText.setText(""+savedInstanceState.getString("antalForkerte"));
            }else{
                antalForkerteText.setText("0");
                antalRigtigeText.setText("0");
            }


            svar1.setOnClickListener(this);
            svar2.setOnClickListener(this);
            svar3.setOnClickListener(this);
            svar4.setOnClickListener(this);
            lavligning();

            return tl;
        }

    @Override
    public void onClick(View view) {

        if(view == svar1 && hvilkenKnapKorrekt == 0 || view == svar2 && hvilkenKnapKorrekt == 1 ||
                view == svar3 && hvilkenKnapKorrekt == 2 || view == svar4 && hvilkenKnapKorrekt == 3){

            showAToast("Korrekt!");
            //Toast.makeText(getActivity(), "Korrekt!", Toast.LENGTH_LONG).show();
            Singleton.point++;
            Singleton.opdater();

            flueben.setImageResource(R.drawable.checkmark1);
            flueben.setVisibility(View.VISIBLE);
            lavligning();

            //Opret en AsyncTask som fjerner Imageviewet efter 2 sekunder
            new AsyncTask(){

                @Override
                protected Object doInBackground(Object[] objects) {
                    try{
                        Thread.sleep(2000);
                        return null;
                    }catch (Exception ex){
                        ex.printStackTrace();
                        return ex;
                    }
                }

                @Override
                protected void onPostExecute(Object data){
                    flueben.setVisibility(View.INVISIBLE);

                }
            }.execute();

            antalRigtigeText.setText(""+(Integer.parseInt(antalRigtigeText.getText().toString())+1));
        }else{
            //Toast.makeText(getActivity(), "Forkert!", Toast.LENGTH_LONG).show();
            showAToast("Forkert!");
            Singleton.point--;
            Singleton.opdater();
            flueben.setImageResource(R.drawable.an_x);
            flueben.setVisibility(View.VISIBLE);
            antalForkerteText.setText(""+(Integer.parseInt(antalForkerteText.getText().toString())+1));
        }

    }


    public void lavligning(){
        //Lav ligning med tilfældige tal
        Random r = new Random();
        int tal1 = r.nextInt(10)+1;
        int tal2 = r.nextInt(10)+1;
        int operator = r.nextInt(3);
        boolean manglendeTalPlacering = r.nextBoolean();
        hvilkenKnapKorrekt = r.nextInt(antalKnapper);
        int result = 0;

        //System.out.println("Tal1 "+tal1+" Tal2: "+tal2+" Op: "+operator+" manglendetalplacering: "+manglendeTalPlacering);

        String ligning = "";
        switch (operator){
            case 0: // +
                result = tal1+tal2;
                if (manglendeTalPlacering) ligning = " _ + " + tal2 + " = " + result;
                else ligning = "" + tal1 + " + _ " + " = " + result;
                break;
            case 1: // -
                result = tal1-tal2;
                if (manglendeTalPlacering) ligning = " _ - " + tal2 + " = " + result;
                else ligning = "" + tal1 + " - " + " _ = " + result;
                break;
            case 2: // *
                result = tal1*tal2;
                if (manglendeTalPlacering) ligning = "_*" + tal2 + "=" + result;
                else ligning = "" + tal1 + " * " + " _ = " + result;
                break;
            /*
            case 3: // % modulus
                result = tal1%tal2;
                if (manglendeTalPlacering) ligning = "_%" + tal2 + "=" + result;
                else ligning = "" + tal1 + "%" + "_=" + result;
                break;
             */
        }


        //Opstætningsfasen
        ligningsfelt.setText(ligning);
        if(manglendeTalPlacering){
            switch (hvilkenKnapKorrekt){
                case 0:
                    svar1.setText(""+tal1);
                    svar2.setText(""+(tal1-1));
                    svar3.setText(""+(tal1-2));
                    svar4.setText(""+(tal1+1));
                    break;
                case 1:
                    svar1.setText(""+(tal1+1));
                    svar2.setText(""+tal1);
                    svar3.setText(""+(tal1-1));
                    svar4.setText(""+(tal1+2));
                    break;
                case 2:
                    svar1.setText(""+(tal1+1));
                    svar2.setText(""+(tal1-1));
                    svar3.setText(""+tal1);
                    svar4.setText(""+(tal1+2));
                    break;
                case 3:
                    svar1.setText(""+(tal1+1));
                    svar2.setText(""+(tal1-1));
                    svar3.setText(""+(tal1+2));
                    svar4.setText(""+tal1);
                    break;
                default:

            }
        }else{
            switch (hvilkenKnapKorrekt){
                case 0:
                    svar1.setText(""+tal2);
                    svar2.setText(""+(tal2-1));
                    svar3.setText(""+(tal2+2));
                    svar4.setText(""+(tal2+1));
                    break;
                case 1:
                    svar1.setText(""+(tal2+1));
                    svar2.setText(""+tal2);
                    svar3.setText(""+(tal2-1));
                    svar4.setText(""+(tal2+2));
                    break;
                case 2:
                    svar1.setText(""+(tal2+1));
                    svar2.setText(""+(tal2-1));
                    svar3.setText(""+tal2);
                    svar4.setText(""+(tal2+2));
                    break;
                case 3:
                    svar1.setText("" + (tal2 + 1));
                    svar2.setText("" + (tal2 - 1));
                    svar3.setText("" + (tal2 + 2));
                    svar4.setText("" + tal2);
                    break;
                default:
            }
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        System.out.println("Jeg blev kaldt og gemte: "+antalRigtigeText.getText().toString()+ " og "+antalForkerteText.getText().toString());
        outState.putString("antalRigtige",antalRigtigeText.getText().toString());
        outState.putString("antalForkerte",antalForkerteText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void showAToast (String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onStop() {
        if(mToast != null){
            mToast.cancel();
        }

        super.onStop();
    }

}