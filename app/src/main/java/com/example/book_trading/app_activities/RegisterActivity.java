package com.example.book_trading.app_activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.book_trading.datenbank.HashHelper;
import com.example.book_trading.R;
import com.example.book_trading.datenbank.User;
import com.example.book_trading.chat.chatLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity um sich zu Registrieren
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * Klassen Attribute
     */
    private EditText UserName, UserPassword;
    private Button BnRegister;
    private String password;
    private boolean accept = false;

    /**
     * Einstiegspunkt für die Aktivity
     * Onclick Listener für den Registrierbutton
     * und Aufruf der Registrierungsfunktion die eine Anfrage an der MySQL-Server schickt
     * zusätzlich gibt es eine abfrage mittels Connectionmanager ob eine Internetverbindung besteht
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = (EditText) findViewById(R.id.txt_user_name);
        UserPassword = (EditText) findViewById(R.id.txt_password);
        final Button RegBtn = (Button) findViewById(R.id.bn_register);
        // beim klicken auf den Registrieren-Button
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // User Registrieren
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() != null) { // wenn verbunden mit Internet
                    performRegistration();
                    if(accept){
                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                } else { // wenn nicht verbunden mit Internet
                    AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alterDialogBuilder.setTitle("KEINE VERBINDUNG!");
                    alterDialogBuilder
                            .setMessage("Keine Verbindung zum Server vorhanden.")
                            .setCancelable(true)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = alterDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    /**
     * Methode für die Registrierung
     * Das eingegebene Passwort wird mittels der HashHelper Klasse verschlüsselt
     * und dann an die Retrofit abfrage übergeben
     * Mittels Retrofit wir eine Abfrage an den MySQL-Server geschickt
     * Es kommt eine Statusmeldung vom server zurück
     */
    public void performRegistration() {
        final String username = UserName.getText().toString();
        String password_klartext = UserPassword.getText().toString();
        try {
            password = HashHelper.encrypt(password_klartext); // verschlüsseln des Textes
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (username.length() == 0 || password.length() == 0) {
            LoginActivity.prefConfig.displayToast("Leereingaben sind ungültig!");
            return;
        }
        Call<User> call = LoginActivity.apiInterface.performRegistration(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("success")) {
                    chatLogin login = new chatLogin(username, password, false, getApplicationContext());
                    LoginActivity.prefConfig.displayToast("Alles Richtig"); // Registrierung hat funktioniert
                    RegisterActivity.this.accept = true;
                } else if (response.body().getResponse().equals("user exists")) {
                    LoginActivity.prefConfig.displayToast("User existiert bereits..."); // es gibt bereits einen User mit gleichem Username
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}