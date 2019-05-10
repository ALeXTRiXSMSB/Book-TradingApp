package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity-Klasse für das eigene Profil
 */

public class ProfilActivity extends AppCompatActivity {

    private Button nachrichtenBtn;
    private Button forumBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);
        this.nachrichtenBtn = (Button) findViewById(R.id.nachrichtenBtn);
        this.forumBtn = (Button) findViewById(R.id.forumBtn);
        this.logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logout();
        nachrichten();
        forum();
    }

    public void logout(){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(logout);
            }
        });
    }

    public void nachrichten(){
        nachrichtenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nachrichten = new Intent(ProfilActivity.this, NachrichtenActivity.class);
                startActivity(nachrichten);
            }
        });
    }

    public void forum(){
        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forum = new Intent(ProfilActivity.this, ForumActivity.class);
                startActivity(forum);
            }
        });
    }

}