package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_trading.chat.chatActivity;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity-Klasse für das Profil anderer Personen
 */
public class ProfilFremdActivity extends AppCompatActivity {
    private TextView positivklick;
    private TextView positivZahl;
    private FloatingActionButton direktChat;

    //TODO: Hier muss der Username der im Forum angeklickt wurde übertragen/überschrieben werden!
    private String fremd_username = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_fremd_layout);

        positivklick = (TextView) findViewById(R.id.positivklick);
        positivZahl = (TextView) findViewById(R.id.positiv);
        direktChat = findViewById(R.id.chat);

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
                        Intent a = new Intent(ProfilFremdActivity.this, chat_uebersichtActivity.class);
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
        direktChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilFremdActivity.this, chatActivity.class);
                intent.putExtra("EMPFAENGER", fremd_username);
                startActivity(intent);
            }
        });
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
                stopService(new Intent(getApplicationContext(), xmppService.class)); //Xmpp wird beim ausloggen disconnectet,
                // somit können Nachrichten die nicht empfangen wurden zum späteren Zeitpunkt abgefragt werden
                Intent logout = new Intent(this, LoginActivity.class);
                LoginActivity.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}