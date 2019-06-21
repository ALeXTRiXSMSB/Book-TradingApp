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
import android.widget.TextView;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.HashHelper;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.R;
import com.example.book_trading.datenbank.User;
import com.example.book_trading.chat.chatLogin;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Activity wenn man das erste mal App startet
 */
public class LoginActivity extends AppCompatActivity {
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        prefConfig.clearAllreadyLiked();
        final TextView RegText = (TextView) findViewById(R.id.register_txt);
        final Button LoginBtn = (Button) findViewById(R.id.login_bn);
        // Registrieren auf TextMessage
        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class); // Öffnen der Register Activity um sich ein Konto zu erstellen
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        // Login-Button klicken um in die App zu gelangen
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() != null) { // wenn mit Internet verbunden dann Methode ausführen
                    performLogin();
                } else { // nicht verbunden mit Internet -> Dialog
                    AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
        // wenn user ist eingeloggt bzw. bereits angemeldet ist und sich beim letzten mal nicht ausgeloggt hat
        if (prefConfig.readLoginStatus()) {
            Intent myIntent = new Intent(LoginActivity.this, ProfilActivity.class);  // öffnen der Profilseite
            LoginActivity.this.startActivity(myIntent);
            chatLogin login = new chatLogin(prefConfig.readName(), prefConfig.readPassword(), true, getApplicationContext());
        }
    }

    /**
     * Methode zum einloggen
     */
    private void performLogin() { // beim klicken auf den Login-Button
        final EditText UserName = (EditText) findViewById(R.id.user_name);
        final EditText Password = (EditText) findViewById(R.id.user_password);
        final String username = UserName.getText().toString();
        String password_klartext = Password.getText().toString();
        try {
            password = HashHelper.encrypt(password_klartext); // verschlüsseln des Textes
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<User> call = LoginActivity.apiInterface.performUserLogin(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) { // Prüfen des Status beim einloggen
                if (response.body().getResponse().equals("success")) { // Status Ok es kann sich eingeloggt werden
                    LoginActivity.prefConfig.writeLoginStatus(true); // LoginStatus auf true setzen um sich einzuloggen
                    LoginActivity.prefConfig.writeName(response.body().getU_name());
                    LoginActivity.prefConfig.writePassword(password);
                    LoginActivity.prefConfig.writeEmail(response.body().getU_email());
                    LoginActivity.prefConfig.writeLikes(response.body().getU_like());
                    LoginActivity.prefConfig.writeFavorites(response.body().getU_favorites());
                    LoginActivity.prefConfig.writeDiscription(response.body().getU_discription());

                    chatLogin login = new chatLogin(username, password, true, getApplicationContext());

                    Intent registerIntent = new Intent(LoginActivity.this, ProfilActivity.class);
                    LoginActivity.this.startActivity(registerIntent); // von der LoginActivity geht es weiter zum Profil bzw. der Startseite

                } else if (response.body().getResponse().equals("no data")) { // StatusFehler ein einloggen ist nicht möglich
                    LoginActivity.prefConfig.displayToast("User name oder Passwort ist falsch");
                } else if (response.body().getResponse().equals("missing argument")) {
                    LoginActivity.prefConfig.displayToast("Beide felder müssen ausgefüllt sein");
                } else if (response.body().getResponse().equals("wrong request method")) {
                    LoginActivity.prefConfig.displayToast("Fehlerhafter request debugging message");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        // nach der Anmeldung die Felder wieder leeren
        UserName.setText("");
        Password.setText("");
    }

}