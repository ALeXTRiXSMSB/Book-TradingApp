package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button forumBtn;
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
        this.forumBtn = (Button)findViewById(R.id.forumBtn);
        positivBtn();
        negativBtn();
        nachrichtBtn();
        forumBtn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profil:
                Intent profil = new Intent(this, ProfilActivity.class);
                startActivity(profil);
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

    public void forumBtn(){
        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forum = new Intent(ProfilFremdActivity.this, ForumActivity.class);
                startActivity(forum);
            }
        });
    }

}