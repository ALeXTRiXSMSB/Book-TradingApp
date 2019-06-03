package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity-Klasse für das eigene Profil
 */

public class ProfilActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    private TextView textViewInfo, textViewMail, textViewBuch, textViewForum,textViewBearbeiten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);
        TextView user = (TextView) findViewById(R.id.txt_name_info);
        user.setText("Hello " + LoginActivity.prefConfig.readName());

        textViewInfo = (TextView) findViewById(R.id.textView_Info);
        textViewMail = (TextView) findViewById(R.id.textView_Mail);
        textViewBuch = (TextView) findViewById(R.id.textView_Buch);
        textViewForum = (TextView) findViewById(R.id.textView_Forum);
        textViewBearbeiten = (TextView) findViewById(R.id.textView_Bearbeiten);

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
                        Intent a = new Intent(ProfilActivity.this, NachrichtenActivity.class);
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
                Intent logout = new Intent(this, LoginActivity.class);
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