package com.example.book_trading.app_activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.book_trading.R;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.datenbank.Thread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity-Klasse für den Forum Eintrag
 */
public class ForumEintragActivity extends AppCompatActivity {
    private Button abbruchBtn;
    private Button verfassenBtn;
    private ItemData selectedItem;
    private String username;
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    private String t_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_eintrag_layout);
        this.abbruchBtn = (Button) findViewById(R.id.abbruchBtn);
        this.verfassenBtn = (Button) findViewById(R.id.verfassenBtn);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Intent activityThatCalled = getIntent();
        Bundle b = activityThatCalled.getExtras();
        if(b!=null){
            username = b.getString("USERNAME");
            t_id = String.valueOf(b.getInt("T_ID"));
            getThread(t_id);
        }

        // Details vom jeweiligen Eintrag
        selectedItem = (ItemData) activityThatCalled.getExtras().getSerializable("data");

        // wenn es noch keinen Inhalt gibt leere alles
        if (selectedItem == null) {
            selectedItem = new ItemData("", "", "", "");
        }else{
            ((EditText) findViewById(R.id.nameEingabe)).setText(selectedItem.Name);
            ((EditText) findViewById(R.id.isbnEingabe)).setText(selectedItem.ISBN);
            ((EditText) findViewById(R.id.zustandEingabe)).setText(selectedItem.Zustand);
            ((EditText) findViewById(R.id.beschreibungEingabe)).setText(selectedItem.Beschreibung);
        }
        // Navigation Bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ForumEintragActivity.this, chat_uebersichtActivity.class);
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

    public void deleteThread(String t_id){
        Call<Thread> call = ForumEintragActivity.apiInterface.performDeleteThread(t_id);
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {

            }

            @Override
            public void onFailure(Call<Thread> call, Throwable t) {

            }
        });
    }

    /**
     * @param t_id
     * holt sich Einträge vom Server
     */
    public void getThread(String t_id){
        Call<Thread> call = ForumEintragActivity.apiInterface.performGetThread(t_id);
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {
                switch(response.body().getResponse()){
                    case "success":{
                        selectedItem = new ItemData(response.body().getT_titel(),response.body().getIsbn(),
                        response.body().getZustand(),response.body().getT_discription());
                        ((EditText) findViewById(R.id.nameEingabe)).setText(selectedItem.Name);
                        ((EditText) findViewById(R.id.isbnEingabe)).setText(selectedItem.ISBN);
                        ((EditText) findViewById(R.id.zustandEingabe)).setText(selectedItem.Zustand);
                        ((EditText) findViewById(R.id.beschreibungEingabe)).setText(selectedItem.Beschreibung);
                        break;
                    }
                    case "no data":{
                        prefConfig.displayToast("No Data");
                        break;
                    }
                    case "missing argument":{
                        prefConfig.displayToast("missing argument");
                        break;
                    }
                    case "wrong request method":{
                        prefConfig.displayToast("wrong request method");
                        break;
                    }
                    default:{

                    }
                }
            }
            @Override
            public void onFailure(Call<Thread> call, Throwable t) {
            }
        });
    }

    /**
     * @param titel
     * @param beschreibung
     * @param isbn
     * @param zustand
     * eingetragenen Daten an den Server
     */
    public void createThread(String titel, String beschreibung, String isbn, String zustand){
        Call<Thread> call = ForumEintragActivity.apiInterface.performCreateThread(titel,beschreibung,isbn, zustand,ForumEintragActivity.prefConfig.readName());
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {
                switch(response.body().getResponse()){
                    case "success":{
                        prefConfig.displayToast("Thread Angelegt");
                        break;
                    }
                    default:{
                        prefConfig.displayToast("Fehler");
                    }
                }
            }

            @Override
            public void onFailure(Call<Thread> call, Throwable t) {
            }
        });
    }

    /**
     * wenn Speicherbutton gedrückt wird
     */
    public void onSaveClick(View view) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) { // verbunden mit Internet
            Intent goingBack = new Intent();

            selectedItem.Name = ((EditText)findViewById(R.id.nameEingabe)).getText().toString();
            selectedItem.ISBN = ((EditText)findViewById(R.id.isbnEingabe)).getText().toString();
            selectedItem.Zustand = ((EditText)findViewById(R.id.zustandEingabe)).getText().toString();
            selectedItem.Beschreibung = ((EditText)findViewById(R.id.beschreibungEingabe)).getText().toString();

            createThread(selectedItem.Name,selectedItem.Beschreibung,selectedItem.ISBN,selectedItem.Zustand);

            goingBack.putExtra("action", "save");
            goingBack.putExtra("data", selectedItem);
            setResult(RESULT_OK, goingBack);
            finish();
        } else { // nicht verbunden mit Internet
            AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(ForumEintragActivity.this);
            alterDialogBuilder.setTitle("KEINE VERBINDUNG!");
            alterDialogBuilder
                    .setMessage("Keine Verbindung zum Server vorhanden.")
                    .setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alertDialog = alterDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * wenn Löschenbutton gedrückt wird
     */
    public void onDeleteClick(View view) {
        // Abfrage ob etwas gelöscht werden soll
        AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(ForumEintragActivity.this);

        alterDialogBuilder.setTitle("Löschen"); // Titel "Löschen" wird angezeigt

        // set dialog message
        alterDialogBuilder
                .setMessage("Soll der Eintrag nun gelöscht werden?")
                .setCancelable(true)
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() { // wenn "Nein" gedrückt wird
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("JA", new DialogInterface.OnClickListener() { // wenn "Ja" gedrückt wird
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goingBack = new Intent();

                        goingBack.putExtra("action", "delete");

                        ForumEintragActivity.this.deleteThread(t_id);
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

}