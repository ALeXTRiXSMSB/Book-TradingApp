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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity-Klasse für Einträge + Chat
 */

public class ForumCommentActivity extends AppCompatActivity {
    private FloatingActionButton btnFremdProfil;
    private FloatingActionButton btnChat;
    public TextView tv_name,tv_zustand,tv_beschreibung,tv_isbn;

    //TODO: Username muss hier unbedingt geholt werden, sonst keine Zuordnung möglich!
    private String fremd_username = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_comment_layout);

        tv_name = findViewById(R.id.tv_name);
        tv_zustand = findViewById(R.id.tv_zustand);
        tv_beschreibung = findViewById(R.id.tv_beschreibung);
        tv_isbn = findViewById(R.id.tv_isbn);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("TID") != null){
            this.editData(bundle.getString("TID"));
        }

        btnFremdProfil = findViewById(R.id.profilFremd);
        btnChat = findViewById(R.id.chat);

        btnFremdProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumCommentActivity.this, ProfilFremdActivity.class);
                startActivity(intent);
            }
        });
        //beim klicken auf den Flieger kann man dem besitzer eine Message schreiben
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumCommentActivity.this, chatActivity.class);
                intent.putExtra("EMPFAENGER", fremd_username);
                startActivity(intent);
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ForumCommentActivity.this, chat_uebersichtActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(ForumCommentActivity.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                        Intent c = new Intent(ForumCommentActivity.this, ForumActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
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

    public void editData(String t_id){
        Call<Thread> call = LoginActivity.apiInterface.performGetThread(t_id);
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {
                switch(response.body().getResponse()){
                    case "success":{
                        tv_name.setText(response.body().getT_titel());
                        tv_beschreibung.setText(response.body().getT_discription());
                        //tv_zustand.setText(response.body().getU_id());
                        //tv_isbn.setText(response.body().getT_id());
                        break;
                    }
                    case "no data":{
                        LoginActivity.prefConfig.displayToast("No Data");
                        break;
                    }
                    case "missing argument":{
                        LoginActivity.prefConfig.displayToast("Missing Argument");
                        break;
                    }
                    case "wrong request method":{
                        LoginActivity.prefConfig.displayToast("wrong Request Method");
                        break;
                    }
                    default:{

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Thread> call, Throwable t) {

            }
        });

    }

}