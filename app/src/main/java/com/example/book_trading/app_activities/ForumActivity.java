package com.example.book_trading.app_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_trading.R;
import com.example.book_trading.datenbank.ApiClient;
import com.example.book_trading.datenbank.ApiInterface;
import com.example.book_trading.datenbank.PrefConfig;
import com.example.book_trading.datenbank.Thread;
import com.example.book_trading.chat.chat_uebersichtActivity;
import com.example.book_trading.chat.xmppService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Activity-Klasse für das Forum
 */

public class ForumActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter = null;
    private ItemData selectedData;
    private static final String TAG = "MainActivity";
    private ArrayList<Thread> listItems = new ArrayList<>();
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        this.lv = (ListView)findViewById(R.id.listView);
        EditText theFilter = (EditText) findViewById(R.id.searchFilter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);   //Fab_Button

        ArrayList<String> names = new ArrayList<>();

        //Navigation am Display ende
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigateNachrichten:
                        Intent a = new Intent(ForumActivity.this, chat_uebersichtActivity.class);
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
        loadForum();

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
                comment.putExtra("TID",String.valueOf(listItems.get(position).getT_id()));
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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
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

    public void loadForum(){
        Call<List<Thread>> call = this.apiInterface.performGetThreads();
        call.enqueue(new Callback<List<Thread>>() {
            @Override
            public void onResponse(Call<List<Thread>> call, Response<List<Thread>> response) {
                for(Thread t: response.body()){
                    adapter.add(t.getT_titel());
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