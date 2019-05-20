package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity-Klasse für das Profil anderer Personen
 */

public class ProfilFremdActivity extends AppCompatActivity {
    private TextView profilName;
    private TextView positiv;
    private TextView negativ;
    private Button positivBtn;
    private Button negativBtn;
    private Button nachrichtBtn;
    private int bewertungCounter;
    private int positivAnzahl;
    private int negativAnzahl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_fremd_layout);
        this.profilName = (TextView)findViewById(R.id.profilName);
        this.positiv = (TextView)findViewById(R.id.positiv);
        this.negativ = (TextView)findViewById(R.id.negativ);
        this.positivBtn = (Button)findViewById(R.id.positivBtn);
        this.negativBtn = (Button)findViewById(R.id.negativBtn);
        this.nachrichtBtn = (Button)findViewById(R.id.nachrichtBtn);
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
        positivBtn();
        negativBtn();
        nachrichtBtn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    @Override
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

    public void positivBtn(){
        positivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativ.setText("0");
                positiv.setText("1");
            }
        });
    }

    public void negativBtn(){
        negativBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiv.setText("0");
                negativ.setText("1");
            }
        });
    }

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