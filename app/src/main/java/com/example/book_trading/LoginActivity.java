package com.example.book_trading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        final TextView RegText = (TextView) findViewById(R.id.register_txt);
        final Button LoginBtn = (Button) findViewById(R.id.login_bn);


        //Registrieren auf TextMessage
        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class); //Öffnen der Register Activity um sich ein Konto zu erstellen
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        //LogIn-Button klicken um in die App zu gelangen
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });



        if(prefConfig.readLoginStatus()){   //wenn user ist eingelogt bzw bereits angemeldet ist und sich beim letzten mal nicht ausgeloggt hat

            Intent myIntent = new Intent(LoginActivity.this, ProfilActivity.class);  //öffnen der Profil seite
            LoginActivity.this.startActivity(myIntent);
        }
    }

    private void performLogin(){    //was passiert beim klicken auf den Login-Button

        final EditText UserName = (EditText) findViewById(R.id.user_name);
        final EditText Password = (EditText) findViewById(R.id.user_password);

        final String username = UserName.getText().toString();
         String password = Password.getText().toString();

        try {
            password = HashHelper.encrypt(password); //verschlüsseln des Textes
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<User> call = LoginActivity.apiInterface.performUserLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {//Prüfen des Status beim einloggen
                if(response.body().getResponse().equals("ok")){ //Status Ok es kann sich eingeloggt werden

                    LoginActivity.prefConfig.writeLoginStatus(true); //LoginStatus auf true setzen um sich ein zu loggen
                    LoginActivity.prefConfig.writeName(response.body().getName());

                    Intent registerIntent = new Intent(LoginActivity.this, ProfilActivity.class);
                    LoginActivity.this.startActivity(registerIntent);   //von der LoginActivity geht es weiter zum Profil bzw. der Startseite

                }
                else if(response.body().getResponse().equals("failed")){    //Status Fehler ein einloggen ist nicht möglich

                    LoginActivity.prefConfig.displayToast("Login Failed. Please try again...");
                }
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        UserName.setText("");   //nach der Anmeldung die Felder wieder leeren
        Password.setText("");
    }





}
