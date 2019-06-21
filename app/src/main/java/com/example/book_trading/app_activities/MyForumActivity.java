package com.example.book_trading.app_activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.book_trading.R;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.datenbank.Thread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyForumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView itemsListView;
    private ArrayList<ItemData> itemArray;
    private ArrayAdapter<ItemData> adapter;
    private ItemData selectedData;
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    private ArrayList<Thread> listItems = new ArrayList<>();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forum);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // Oberflächenelement
        itemsListView = findViewById(R.id.listView);

        itemArray = new ArrayList<ItemData>();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1); // Fab_Button

        try{
            LoadData(prefConfig.readName()); // Beispieldaten laden
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        // Navigation am Display-ende
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(MyForumActivity.this, chat_uebersichtActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(MyForumActivity.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                        Intent c = new Intent(MyForumActivity.this, ForumActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
        // Adapter für die ListView
        adapter = new ArrayAdapter<ItemData>(this, android.R.layout.simple_list_item_1,itemArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // float size = Float.valueOf(textSize);

                // set the text size 25 dip for ListView each item

                // tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);

                // Return the view
                return view;
            }
        };
        // Verbindung Oberfläche mit dem Adapter
        itemsListView.setAdapter(adapter);

        // der auf den ListView-Click hörende ist diese Klasse
        itemsListView.setOnItemClickListener(this);

        // klicken des kleinen Plus
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * @param menu
     * Menu-Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    /**
     * @param item
     * klicken auf logout
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

    /**
     * Methode um neues Item hinzuzufügen
     */
    private void AddNewItem(){
        Intent getDetailIntent = new Intent(this,
                ForumEintragActivity.class);

        final int result = 1;

        selectedData = null;
        getDetailIntent.putExtra("data", selectedData);
        getDetailIntent.putExtra("USERNAME",username);

        startActivityForResult(getDetailIntent, result);
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     * Methode wenn Eintrag angeklickt wurde
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent getDetailIntent = new Intent(this,
                ForumEintragActivity.class); //Öffnen der Klasse um den Eintrag zu bearbeiten

        final int result = 1;

        selectedData = itemArray.get(position);
        getDetailIntent.putExtra("data", selectedData);
        getDetailIntent.putExtra("T_ID",this.listItems.get(position).getT_id());
        startActivityForResult(getDetailIntent, result);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * nach dem beenden des Eintrages
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED && data != null) {

            String sentBack = data.getStringExtra("action"); // Inhalte zurückgeben

            ItemData item = (ItemData) data.getExtras().getSerializable("data");

            switch (sentBack) { // was wurde gewählt "Löschen" oder "Speichern"
                case "delete": {
                    if (itemArray.contains(selectedData))
                        adapter.remove(selectedData);
                    break;
                }
                case "save": {
                    if (item != null) { // hinzufügen
                        if (selectedData == null)
                            adapter.add(item);
                        else { // INSERT => ersetzen der alten durch die neuen Daten
                            int index = itemArray.indexOf(selectedData);
                            adapter.insert(item, index);
                            adapter.remove(selectedData);
                        }
                    }
                    break;
                }
            }
        }
        selectedData = null;
        adapter.notifyDataSetChanged();
    }

    /**
     * Methode um die eigenen ForumEinträge vom Server zu holen
     */
    private void LoadData(String username){
        Call<List<Thread>> call = LoginActivity.apiInterface.performGetOwnThreads(username);
        call.enqueue(new Callback<List<Thread>>() {
            @Override
            public void onResponse(Call<List<Thread>> call, Response<List<Thread>> response) {
                for(Thread t: response.body()){
                    adapter.add(new ItemData(t.getT_titel()));
                }
                for(Thread t:response.body()){
                    listItems.add(t);
                }
            }
            @Override
            public void onFailure(Call<List<Thread>> call, Throwable t) {
            }
        });
    }

}