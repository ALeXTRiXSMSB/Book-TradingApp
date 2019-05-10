package com.example.book_trading;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity-Klasse für Kommentierung im Forum
 */

public class ForumCommentActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter = null;
    private EditText textEingabe;
    private Button sendenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_comment_layout);
        this.lv = (ListView)findViewById(R.id.listView);
        this.textEingabe = (EditText)findViewById(R.id.textEingabe);
        this.sendenBtn = (Button)findViewById(R.id.eingabeSenden);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);
        test();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent fprofil = new Intent(view.getContext(), ProfilFremdActivity.class);
                startActivity(fprofil);
            }
        });
        sendenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textEingabe.getText().toString() != null) {
                    adapter.add(textEingabe.getText().toString());
                    textEingabe.setText("");
                } else {
                }
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
        adapter.add("Test-Profil1");
        adapter.add("Test-Profil3");
        adapter.add("Test-Profil3");
    }



}