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

public class MainActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button registrierenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.loginBtn = (Button) findViewById(R.id.login);
        this.registrierenBtn = (Button) findViewById(R.id.registrieren);
        registrierenBtn.setEnabled(false);
        login();
    }

    public void login(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profil = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(profil);
            }
        });
    }

    public void registrieren(){
        registrierenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}