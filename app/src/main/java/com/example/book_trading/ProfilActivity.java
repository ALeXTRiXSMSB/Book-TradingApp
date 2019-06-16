package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity-Klasse für das eigene Profil
 */
public class ProfilActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private static PrefConfig prefConfig;
    private static ApiInterface apiInterface;
    private TextView textViewInfo, textViewMail, textViewBuch, textViewForum,textViewBearbeiten,textviewLike,user;

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
        textviewLike = (TextView) findViewById(R.id.positiv);
        textViewForum = (TextView) findViewById(R.id.textView_Forum);

        textviewLike.setText(this.prefConfig.readLikes());

        user.setText("Hello " + this.prefConfig.readName());
        this.applyTexts(this.prefConfig.readDiscription(),this.prefConfig.readEmail(),this.prefConfig.readFavorites());

        //beim klicken auf die bearbeiten um seine Profildaten zu bearbeiten
        textViewBearbeiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //Klicken auf Forum um zu seinen eigenen Forum Produkten zu kommen
        textViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, my_forum.class);
                startActivity(intent);
            }
        });
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

    //menu-Bar anlegen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    //Menu-Barklicken
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                stopService(new Intent(getApplicationContext(), xmppService.class)); //Xmpp wird beim ausloggen disconnectet,
                // somit können Nachrichten die nicht empfangen wurden zum späteren Zeitpunkt abgefragt werden
                Intent logout = new Intent(this, LoginActivity.class);
                this.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Dialogfensteröffnen
    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    //Dialog
    @Override
    public void applyTexts(String info, String mail, String buch) {
        textViewInfo.setText(info);
        textViewMail.setText(mail);
        textViewBuch.setText(buch);
    }

}