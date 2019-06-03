package com.example.book_trading;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity-Klasse für den Forum Eintrag
 */

public class ForumEintragActivity extends AppCompatActivity {
    private Button abbruchBtn;
    private Button verfassenBtn;
    private ItemData selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_eintrag_layout);
        this.abbruchBtn = (Button)findViewById(R.id.abbruchBtn);
        this.verfassenBtn = (Button)findViewById(R.id.verfassenBtn);

        //////////////////////////////
        Intent activityThatCalled = getIntent();

        selectedItem = (ItemData)activityThatCalled.getExtras().getSerializable("data");
        //Wenn es noch keinen Inhalt gibt leere alles
        if (selectedItem == null)
        {
            selectedItem = new ItemData("","","","" );
        }

        ((EditText)findViewById(R.id.nameEingabe)).setText(selectedItem.Name);
        ((EditText)findViewById(R.id.isbnEingabe)).setText(selectedItem.ISBN);
        ((EditText)findViewById(R.id.zustandEingabe)).setText(selectedItem.Zustand);
        ((EditText)findViewById(R.id.beschreibungEingabe)).setText(selectedItem.Beschreibung);

        //////////////////////////////

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ForumEintragActivity.this, NachrichtenActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(ForumEintragActivity.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                        Intent c = new Intent(ForumEintragActivity.this, ForumActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

    }

    //////////////////////
    //Speichern
    public void onSaveClick(View view) {
        Intent goingBack = new Intent();

        selectedItem.Name = ((EditText)findViewById(R.id.nameEingabe)).getText().toString();
        selectedItem.ISBN = ((EditText)findViewById(R.id.isbnEingabe)).getText().toString();
        selectedItem.Zustand = ((EditText)findViewById(R.id.zustandEingabe)).getText().toString();
        selectedItem.Beschreibung = ((EditText)findViewById(R.id.beschreibungEingabe)).getText().toString();

        goingBack.putExtra("action", "save");
        goingBack.putExtra("data", selectedItem);

        setResult(RESULT_OK, goingBack);

        finish();
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);

        finish();
    }

    //Löschen
    public void onDeleteClick(View view) {
        //Abfrage ob etwas gelöscht werden soll
        AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(ForumEintragActivity.this);

        alterDialogBuilder.setTitle("Löschen"); //Titel Löschen wird angezeigt

        //Set dialog message
        alterDialogBuilder
                .setMessage("Soll der Eintrag nun gelöscht werden?")
                .setCancelable(true)
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goingBack = new Intent();

                        goingBack.putExtra("action", "delete");
                        //goingBack.putExtra("data", selectedItem);

                        setResult(RESULT_OK, goingBack);

                        finish();
                    }
                });
        AlertDialog alertDialog = alterDialogBuilder.create();
        alertDialog.show();

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
                Intent logout = new Intent(this, LoginActivity.class);
                LoginActivity.prefConfig.writeLoginStatus(false);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}