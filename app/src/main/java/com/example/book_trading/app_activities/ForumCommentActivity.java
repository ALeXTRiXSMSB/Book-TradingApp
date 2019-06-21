package com.example.book_trading.app_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.book_trading.R;
import com.example.book_trading.datenbank.Thread;
import com.example.book_trading.chat.chatActivity;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity-Klasse für die Details der Forumeinträge
 */
public class ForumCommentActivity extends AppCompatActivity {
    private FloatingActionButton btnFremdProfil;
    private FloatingActionButton btnChat;
    public TextView tv_name, tv_zustand, tv_beschreibung, tv_isbn, tv_fremdName;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_comment_layout);

        tv_name = findViewById(R.id.tv_name);
        tv_zustand = findViewById(R.id.tv_zustand);
        tv_beschreibung = findViewById(R.id.tv_beschreibung);
        tv_isbn = findViewById(R.id.tv_isbn);
        tv_fremdName = findViewById((R.id.tv_FremdName));

        // Daten an den Server
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("TID") != null) {
            this.editData(bundle.getString("TID"));
        }

        btnFremdProfil = findViewById(R.id.profilFremd);
        btnChat = findViewById(R.id.chat);

        // wenn man hier klickt kommt man auf das Profil von demjenigen der den Eintrag erstellt hat
        btnFremdProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn man selber der Ersteller ist, kommt man auf das eigene Profil
                if (tv_fremdName.getText().toString().equals(ProfilActivity.prefConfig.readName())) {
                    Intent b = new Intent(ForumCommentActivity.this, ProfilActivity.class);
                    startActivity(b);
                } else {
                    Intent intent = new Intent(ForumCommentActivity.this, ProfilFremdActivity.class);
                    intent.putExtra("USERNAME", name);
                    startActivity(intent);
                }
            }
        });
        // beim klicken auf den Flieger kann man dem Ersteller eine Nachricht schreiben
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // wenn man selber Ersteller ist, kommt man zu den Nachrichten
                    if (tv_fremdName.getText().toString().equals(ProfilActivity.prefConfig.readName())) {
                        Intent a = new Intent(ForumCommentActivity.this, chat_uebersichtActivity.class);
                        startActivity(a);
                    } else {
                        Intent intent = new Intent(ForumCommentActivity.this, chatActivity.class);
                        intent.putExtra("EMPFAENGER", name);
                        startActivity(intent);
                    }
            }
        });
        // NavigationBar ganz unten
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
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
                ForumActivity.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param t_id Id der jeweiligen Einträge
     *             Methode für Daten aus der Datenbank an die Activity
     */
    public void editData(String t_id) {
        Call<Thread> call = ForumActivity.apiInterface.performGetThread(t_id);
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {
                switch (response.body().getResponse()) {
                    case "success": {
                        tv_name.setText(response.body().getT_titel());
                        tv_beschreibung.setText(response.body().getT_discription());
                        tv_isbn.setText(response.body().getIsbn());
                        tv_zustand.setText(response.body().getZustand());
                        name = response.body().getU_name();
                        tv_fremdName.setText(response.body().getU_name());
                        break;
                    }
                    case "no data": {
                        LoginActivity.prefConfig.displayToast("No Data");
                        break;
                    }
                    case "missing argument": {
                        LoginActivity.prefConfig.displayToast("Missing Argument");
                        break;
                    }
                    case "wrong request method": {
                        LoginActivity.prefConfig.displayToast("wrong Request Method");
                        break;
                    }
                    default: {

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