package com.example.book_trading;

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

import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class my_forum extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView itemsListView;
    private ArrayList<ItemData> itemArray;
    private ArrayAdapter<ItemData> adapter;
    private ItemData selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forum);
       //Oberflächenelement
        itemsListView = findViewById(R.id.listView);

        itemArray = new ArrayList<ItemData>();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);   //Fab_Button

        LoadData(); //BeispielDaten laden

        //Navigation am Display ende
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(my_forum.this, chat_uebersichtActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(my_forum.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                         Intent c = new Intent(my_forum.this, ForumActivity.class);
                         startActivity(c);
                        break;
                }
                return false;
            }
        });

        adapter = new ArrayAdapter<ItemData>(this, android.R.layout.simple_list_item_1,itemArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

               // TextView tv = (TextView) view.findViewById(android.R.id.text1);

               // float size = Float.valueOf(textSize);

                // Set the text size 25 dip for ListView each item

               // tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);

                // Return the view
                return view;
            }
        };
        //Verbindung Oberfläche mit dem Adapter
        itemsListView.setAdapter(adapter);

        //Der auf den ListView-Click hörende ist diese Klasse
        itemsListView.setOnItemClickListener(this);

        //klicken des kleinen Plus
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

    //Menu-button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    //klicken auf den Outlock button
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

    //neues Item Hinzufühgen
   private void AddNewItem(){
        Intent getDetailIntent = new Intent(this,
                ForumEintragActivity.class);

        final int result = 1;

        selectedData = null;
        getDetailIntent.putExtra("data", selectedData);

        startActivityForResult(getDetailIntent, result);
    }

    //Eintrag anklicken
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent getDetailIntent = new Intent(this,
                ForumEintragActivity.class); //Öffnen der Klasse um den Eintrag zu bearbeiten

        final int result = 1;

        selectedData = itemArray.get(position);
        getDetailIntent.putExtra("data", selectedData);

        startActivityForResult(getDetailIntent, result);
    }

    @Override //Nach dem Beenden des Eintrages
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED && data != null) {

            String sentBack = data.getStringExtra("action");    //Inhalte zurück geben

            ItemData item = (ItemData) data.getExtras().getSerializable("data");

            switch (sentBack) { //was wurde gewählt Löschen oder Speichern
                case "delete": {
                    if (itemArray.contains(selectedData))
                        adapter.remove(selectedData);
                    break;
                }
                case "save": {
                    if (item != null) {
                        //ADD
                        if (selectedData == null)
                            adapter.add(item);
                            //INSERT => ersetzen der alten durch die neuen Daten
                        else {
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

    //TestEinträge
    //Beispieldaten einfühgen
    private void LoadData(){
        //Beispieldaten
        ItemData item1 = new ItemData("Buch 1", "1234", "schlecht","wie neu");
        ItemData item2 = new ItemData("Buch 2", "2345", "gut","nie benutzt");
        ItemData item3 = new ItemData("Buch 3", "3456", "sehr gut","tolles Buch");

        itemArray = new ArrayList();
        itemArray.add(item1);
        itemArray.add(item2);
        itemArray.add(item3);
    }

}