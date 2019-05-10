package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity-Klasse für das Forum
 */

public class ForumActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter = null;
    private Button eintragBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);
        this.lv = (ListView)findViewById(R.id.listView);
        this.eintragBtn = (Button)findViewById(R.id.neuerEintrag);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);
        test();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent comment = new Intent(view.getContext(), ForumCommentActivity.class);
                startActivity(comment);
            }
        });
        eintragBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent neintrag = new Intent(ForumActivity.this, ForumEintragActivity.class);
                startActivity(neintrag);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profil:
                Intent profil = new Intent(this, ProfilActivity.class);
                startActivity(profil);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void test(){
        adapter.add("Test-Eintrag1");
        adapter.add("Test-Eintrag2");
        adapter.add("Test-Eintrag3");
        adapter.add("Test-Eintrag4");
    }

}