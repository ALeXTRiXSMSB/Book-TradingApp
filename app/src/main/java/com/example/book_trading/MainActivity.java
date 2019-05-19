package com.example.book_trading;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * das ist der Login (zum Testen)
 * und Start der App
 */

public class MainActivity extends AppCompatActivity{

    //public static PrefConfig prefConfig;
    //public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prefConfig = new PrefConfig(this);
        //apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Intent intent = new Intent(MainActivity.this, LoginActivity.class);  //öffnen der Profil seite
        MainActivity.this.startActivity(intent);

        /*if(findViewById(R.id.fragment_container)!=null){    //wenn User bereits eingelogt dann zeige Startseite ansonsten erst anmelden

            if(savedInstanceState!=null){

                return;
            }*/




            //Zeigt immer das LogIn im Hauptfenster an
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();






            /*if(prefConfig.readLoginStatus()){   //wenn user ist eingelogt bzw bereits angemeldet ist und sich beim letzten mal nicht ausgeloggt hat

                Intent myIntent = new Intent(MainActivity.this, ProfilActivity.class);  //öffnen der Profil seite
                MainActivity.this.startActivity(myIntent);
            }*/
        }
    }

