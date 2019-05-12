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

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFormActivityListener{

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        if(findViewById(R.id.fragment_container)!=null){    //wenn User bereits eingelogt dann zeige Startseite ansonsten erst anmelden

            if(savedInstanceState!=null){

                return;
            }

            //Zeigt immer das LogIn im Hauptfenster an
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();

            if(prefConfig.readLoginStatus()){   //wenn user ist eingelogt

                Intent myIntent = new Intent(MainActivity.this, ProfilActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        }
    }

    @Override
    public void performRegister() { //wenn User auf TextView klickt zum Registrieren geht die neue Activity auf
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void performLogin(String name) {

        prefConfig.writeName(name);

        Intent myIntent = new Intent(MainActivity.this, ProfilActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

}