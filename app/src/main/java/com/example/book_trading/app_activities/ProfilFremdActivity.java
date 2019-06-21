package com.example.book_trading.app_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.book_trading.R;
import com.example.book_trading.chat.chatActivity;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.datenbank.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity-Klasse für das Profil anderer Personen
 */
public class ProfilFremdActivity extends AppCompatActivity {
    private TextView positivklick;
    private TextView positivZahl;
    private FloatingActionButton direktChat;
    private boolean likeClicked = false;
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    public TextView tv_eintraege, textView_Info, textView_Mail, textView_Buch, txt_name_info;
    public String fremd_username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_fremd_layout);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        positivklick = (TextView) findViewById(R.id.positivklick);
        positivZahl = (TextView) findViewById(R.id.tv_positiv);
        direktChat = findViewById(R.id.chat);

        tv_eintraege = findViewById(R.id.tv_eintraege);
        textView_Info = findViewById(R.id.textView_Info);
        textView_Mail = findViewById(R.id.textView_Mail);
        textView_Buch = findViewById(R.id.textView_Buch);
        txt_name_info = findViewById(R.id.txt_name_info);


        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b.getString("USERNAME") != null) {
            this.loadData(b.getString("USERNAME"));
        }

        // beim klicken auf den Daumen
        positivklick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeklicken();
            }
        });
        // NavigationBar unten
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
        // beim klicken auf den Chat-Button
        direktChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilFremdActivity.this, chatActivity.class);
                intent.putExtra("EMPFAENGER", fremd_username);
                startActivity(intent);
            }
        });
    }

    /**
     * Methode zum liken
     * wenn man das jeweilige Profil noch nicht geliket hat kann man es liken
     */
    private void likeklicken() {
        String[] liked = ProfilFremdActivity.prefConfig.readAlleadyLiked().split(",");
        for(int i = 0; i < liked.length;i++){
            if(liked[i].matches(fremd_username)){
                this.likeClicked = true;
            }
        }
        positivZahl = findViewById(R.id.tv_positiv);
        if (!this.likeClicked) {
            Call<Integer> call = ProfilFremdActivity.apiInterface.performinclike(this.fremd_username);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    loadData(fremd_username);
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                }
            });
            int tmp = Integer.valueOf(this.positivZahl.getText().toString());
            this.positivZahl.setText(String.valueOf(tmp+1));
            ProfilFremdActivity.prefConfig.writeAllreadyLiked(this.fremd_username);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // logout
        switch (item.getItemId()) {
            case R.id.logout:
                stopService(new Intent(getApplicationContext(), xmppService.class)); // Xmpp wird beim ausloggen disconnected,
                // somit können Nachrichten die nicht empfangen wurden zum späteren Zeitpunkt abgefragt werden
                Intent logout = new Intent(this, LoginActivity.class);
                LoginActivity.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Methode für die Aktualisierung des FremdProfils
     */
    public void loadData(String username) {
        Call<String> callUpdate = ProfilFremdActivity.apiInterface.performCount(username);
        callUpdate.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String tmp = response.body().toString();
                ProfilFremdActivity.this.tv_eintraege.setText(tmp);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        Call<User> call = ProfilFremdActivity.apiInterface.performGetProfile(username);
        this.fremd_username = username;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.body().getResponse()) {
                    case "success": {
                        txt_name_info.setText(response.body().getU_name());
                        textView_Buch.setText(response.body().getU_favorites());
                        textView_Mail.setText(response.body().getU_email());
                        textView_Info.setText(response.body().getU_discription());
                        positivZahl.setText(response.body().getU_like());
                        break;
                    }
                    case "no data": {
                        prefConfig.displayToast("User nicht Vorhanden");
                        break;
                    }
                    case "missing argument": {
                        prefConfig.displayToast("Missing Argument");
                        break;
                    }
                    case "no request method": {
                        prefConfig.displayToast("Keine Ahnung");
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

}