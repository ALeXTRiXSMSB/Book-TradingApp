package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity-Klasse für das Profil anderer Personen
 */

public class ProfilFremdActivity extends AppCompatActivity {


    private TextView positivklick;
    private TextView positivZahl;

    private TextView nachrichtBtn;


    private int bewertungCounter;
    private int positivAnzahl;
    private int negativAnzahl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_fremd_layout);

        positivklick = (TextView) findViewById(R.id.positivklick);
        positivZahl = (TextView) findViewById(R.id.positiv);
        nachrichtBtn = (TextView) findViewById(R.id.textView_Mail);


        positivklick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeklicken();
            }
        });



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ProfilFremdActivity.this, NachrichtenActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(ProfilFremdActivity.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                        Intent c = new Intent(ProfilFremdActivity.this, ForumActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
       // positivBtn();

        nachrichtBtn();
    }

    //Setzen der Zahl unter dem like auf 1
    private void likeklicken() {

        positivZahl.setText("1");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    @Override   //Ausloggen
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



            //auf Nachricht schreiben klicken
    public void nachrichtBtn(){
        nachrichtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat = new Intent(ProfilFremdActivity.this, ChatActivity.class);
                startActivity(chat);
            }
        });
    }

}