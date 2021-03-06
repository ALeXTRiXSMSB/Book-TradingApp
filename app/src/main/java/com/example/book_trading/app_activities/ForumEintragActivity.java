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
import android.widget.Toast;

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
    /**
     * Klassen Attribute
     */
    private Button abbruchBtn;
    private Button verfassenBtn;
    private ItemData selectedItem;
    private String username;
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    private String t_id;
    private boolean exists = false;

    /**
     * Einstiegspunkt für die Activity
     * sollten sich zusätzliche Daten in dem Intent befinden aus dem die Activity aufgerufen wird
     * werden diese mittels Anfrage an den MySQL Server abgefragt
     * Daten aus der Abfrage werden in die Activity geladen
     * @param savedInstanceState
     */
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
            exists = true;
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

    /**
     * Methode zum Löschen eines Threads
     * @param t_id
     */
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
     * Anfrage an den Server damit in der Activity die Korrekten Daten geladen werden
     * wenn welche vorhanden sind
     * wird nur aufgerufen wenn daten beim Activity Aufruf mit gegeben wurden
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
     * eingetragenen Daten an den Server übermitteln damit in der datenbank ein neuer thread angelegt wird
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
     * Methode für ein Update eines bereits vorhandenen Threads
     * Keine behandlung von status meldungen
     * @param name
     * @param beschreibung
     * @param isbn
     * @param zustand
     */
    public void updateThread(String name, String beschreibung, String isbn,String zustand){
        Call<Thread> call = ForumEintragActivity.apiInterface.performUpdateThread(name,beschreibung,isbn,zustand,t_id);
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
     * wenn Speicherbutton gedrückt wird
     * entweder wird die createmethode aufgerufen oder die update methode
     * wenn die Activity leer aufgerufen wurde wird ein neuer Thread erstellt
     * wenn die Activity mit daten aufgerufen wird bekommt der Thread auf dem
     * MySQL Server ein update der Daten
     */
    public void onSaveClick(View view) {
        EditText name = findViewById(R.id.nameEingabe);
        EditText isbn = findViewById(R.id.isbnEingabe);
        EditText zustand = findViewById(R.id.zustandEingabe);
        EditText beschreibung = findViewById(R.id.beschreibungEingabe);
        if(!name.getText().toString().isEmpty() ||
                !isbn.getText().toString().isEmpty() ||
                !zustand.getText().toString().isEmpty() ||
                !beschreibung.getText().toString().isEmpty() ){

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) { // verbunden mit Internet
                Intent goingBack = new Intent();

                selectedItem.Name = ((EditText) findViewById(R.id.nameEingabe)).getText().toString();
                selectedItem.ISBN = ((EditText) findViewById(R.id.isbnEingabe)).getText().toString();
                selectedItem.Zustand = ((EditText) findViewById(R.id.zustandEingabe)).getText().toString();
                selectedItem.Beschreibung = ((EditText) findViewById(R.id.beschreibungEingabe)).getText().toString();

                if(exists){
                    updateThread(selectedItem.Name, selectedItem.Beschreibung, selectedItem.ISBN, selectedItem.Zustand);
                }else{
                    createThread(selectedItem.Name, selectedItem.Beschreibung, selectedItem.ISBN, selectedItem.Zustand);
                }

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
        }else{
            Toast.makeText(this,"Bitte alle Felder Ausfüllem",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * beenden der Activity ohne änderungen vorgenommen zu haben
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * wenn Löschenbutton gedrückt wird
     * Aufruf der Löschen funktion damit der Server die jetzigen eintrag auch löscht
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

    /**
     * Methode zum erstellen des Menüs
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    /**
     * Logout Methode Login Activity wird geladen
     * @param item
     * @return
     */
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