package com.example.book_trading.app_activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText UserName, UserPassword;
    private Button BnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = (EditText) findViewById(R.id.txt_user_name);
        UserPassword = (EditText) findViewById(R.id.txt_password);
        final Button RegBtn = (Button) findViewById(R.id.bn_register);
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //User Registrieren
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() != null) { // verbunden
                    performRegistration();
                } else { // nicht verbunden
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

    private String password;

    public void performRegistration() {
        final String username = UserName.getText().toString();
        String password_klartext = UserPassword.getText().toString();
        try {
            password = HashHelper.encrypt(password_klartext); //verschlüsseln des Textes
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (username.length() == 0 || password.length() == 0) {
            LoginActivity.prefConfig.displayToast("Leereingaben sind ungültig!");
            return;
        }
        Call<User> call = LoginActivity.apiInterface.performRegistration(username, password);// alles ab hier nach unten
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("success")) {
                    chatLogin login = new chatLogin(username, password, false, getApplicationContext());
                    LoginActivity.prefConfig.displayToast("Alles Richtig");    //Registrierung hat funktioniert
                    //springe zurück
                } else if (response.body().getResponse().equals("user exists")) {
                    LoginActivity.prefConfig.displayToast("User already exist..."); //Es gibt bereits einen User mit gleichem Username
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        //Nach dem anmelden werden die Felder wieder geleert und es können neue Inhalte eingetragen werden
        //UserPassword.setText("");
        //UserName.setText("");
    }

}