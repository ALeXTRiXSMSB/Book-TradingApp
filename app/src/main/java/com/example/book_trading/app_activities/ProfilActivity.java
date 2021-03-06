package com.example.book_trading.app_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.R;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.example.book_trading.datenbank.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity-Klasse für das eigene Profil
 */
public class ProfilActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    public TextView textViewInfo, textViewMail, textViewBuch, textViewForum,textViewBearbeiten,textviewLike,user,textview_forum;

    /**
     * diese Methode wird beim Start der Activity aufgerufen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        user = (TextView) findViewById(R.id.txt_name_info);
        textViewInfo = (TextView) findViewById(R.id.textView_Info);
        textViewMail = (TextView) findViewById(R.id.textView_Mail);
        textViewBuch = (TextView) findViewById(R.id.textView_Buch);
        textViewBearbeiten = (TextView) findViewById(R.id.textView_Bearbeiten);
        textviewLike = (TextView) findViewById(R.id.tv_positiv);
        textViewForum = (TextView) findViewById(R.id.textView_Forum);
        textview_forum = findViewById(R.id.textview_forum);

        update();
        textviewLike.setText(this.prefConfig.readLikes());
        user.setText("Hallo " + this.prefConfig.readName());
        this.applyTexts(this.prefConfig.readDiscription(),this.prefConfig.readEmail(),this.prefConfig.readFavorites());

        // beim klicken auf das Symbol zum bearbeiten der Profildaten
        textViewBearbeiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // klicken auf Forum um zu seinem eigenen Forum zu kommen
        textViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, MyForumActivity.class);
                startActivity(intent);
            }
        });
        // NavigationBar unten
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ProfilActivity.this, chat_uebersichtActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        // Intent b = new Intent(ProfilActivity.this, ProfilActivity.class);
                        // startActivity(b);
                        break;
                    case R.id.navigateForum:
                        Intent c = new Intent(ProfilActivity.this, ForumActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * @param menu
     * Menu für das Logout
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    /**
     * @param item
     * wenn im Menu auf das Item geklickt wird -> Logout
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                stopService(new Intent(getApplicationContext(), xmppService.class)); // Xmpp wird beim ausloggen disconnected,
                // somit können Nachrichten die nicht empfangen wurden zum späteren Zeitpunkt abgefragt werden
                Intent logout = new Intent(this, LoginActivity.class);
                this.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Methode um das Dialogfenster zu öffnen
     */
    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog(textViewInfo.getText().toString(),
                textViewBuch.getText().toString(),
                textViewMail.getText().toString());
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    /**
     * @param info
     * @param mail
     * @param buch
     * für die Profilbeschreibung
     */
    @Override
    public void applyTexts(String info, String mail, String buch) {
        textViewInfo.setText(info);
        textViewMail.setText(mail);
        textViewBuch.setText(buch);

        Call<User> call = ProfilActivity.apiInterface.performEditProfile(mail,info,buch,ProfilActivity.prefConfig.readName());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                update();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    /**
     * Methode für die Aktualisierung des Profils
     */
    public void update(){
        Call<String> call = ProfilActivity.apiInterface.performCount(ProfilActivity.prefConfig.readName());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String tmp = response.body().toString();
                ProfilActivity.this.textview_forum.setText(tmp);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        Call<User> callUpdate2 = ProfilActivity.apiInterface.performGetProfile(ProfilActivity.prefConfig.readName());
        callUpdate2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("success")) { // Status Ok es kann sich eingeloggt werden
                    ProfilActivity.prefConfig.writeName(response.body().getU_name());
                    ProfilActivity.prefConfig.writeEmail(response.body().getU_email());
                    ProfilActivity.prefConfig.writeLikes(response.body().getU_like());
                    ProfilActivity.prefConfig.writeFavorites(response.body().getU_favorites());
                    ProfilActivity.prefConfig.writeDiscription(response.body().getU_discription());
                    ProfilActivity.this.textViewMail.setText(ProfilActivity.prefConfig.readEmail());
                    ProfilActivity.this.textViewInfo.setText(ProfilActivity.prefConfig.readDiscription());
                    ProfilActivity.this.textViewBuch.setText(ProfilActivity.prefConfig.readFavorites());
                    ProfilActivity.this.textviewLike.setText(ProfilActivity.prefConfig.readLikes());

                } else if (response.body().getResponse().equals("no data")) { // Status Fehler ein einloggen ist nicht möglich
                    ProfilActivity.prefConfig.displayToast("User name oder Passwort ist falsch");
                } else if (response.body().getResponse().equals("missing argument")) {
                    ProfilActivity.prefConfig.displayToast("Beide felder müssen ausgefüllt sein");
                } else if (response.body().getResponse().equals("wrong request method")) {
                    ProfilActivity.prefConfig.displayToast("Fehlerhafter request debugging message");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

}