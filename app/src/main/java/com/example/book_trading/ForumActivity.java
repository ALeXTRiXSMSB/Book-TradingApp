package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Activity-Klasse für das Forum
 */

public class ForumActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter = null;
    //private Button eintragBtn;
    private ItemData selectedData;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);
        this.lv = (ListView)findViewById(R.id.listView);
        EditText theFilter = (EditText) findViewById(R.id.searchFilter);
        //this.eintragBtn = (Button)findViewById(R.id.neuerEintrag);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);   //Fab_Button

        ArrayList<String> names = new ArrayList<>();

        //Navigation am Display ende
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ForumActivity.this, NachrichtenActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigateProfil:
                        Intent b = new Intent(ForumActivity.this, ProfilActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigateForum:
                        // Intent c = new Intent(ForumActivity.this, ForumActivity.class);
                        // startActivity(c);
                        break;
                }
                return false;
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        test();

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ForumActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //klicken der einträge
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent comment = new Intent(view.getContext(), ForumCommentActivity.class);
                startActivity(comment);
            }
        });

        //klicken des kleinen Plus
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem();
                /*Intent neintrag = new Intent(ForumActivity.this, ForumEintragActivity.class);
                startActivity(neintrag);*/
            }
        });
    }


    private void AddNewItem(){
        Intent getDetailIntent = new Intent(this,
                ForumEintragActivity.class);

        final int result = 1;

        selectedData = null;
        getDetailIntent.putExtra("data", selectedData);

        startActivityForResult(getDetailIntent, result);
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

    public void test(){
        adapter.add("Buch 1");
        adapter.add("Buch 2");
        adapter.add("Buch 3");
        adapter.add("Zeitschrift 1");
        adapter.add("Zeitschrift 2");
        adapter.add("Zeitschrift 3");
        adapter.add("Das Buch 1");
        adapter.add("Das Buch 2");
    }

}