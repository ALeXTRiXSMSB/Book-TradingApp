package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity-Klasse für den Forum Eintrag
 */

public class ForumEintragActivity extends AppCompatActivity {
    private Button abbruchBtn;
    private Button verfassenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_eintrag_layout);
        this.abbruchBtn = (Button)findViewById(R.id.abbruchBtn);
        this.verfassenBtn = (Button)findViewById(R.id.verfassenBtn);
        abbruchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        verfassenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

}