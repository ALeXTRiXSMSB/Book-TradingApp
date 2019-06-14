package com.example.book_trading.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.book_trading.R;

import java.util.ArrayList;

public class chat_uebersichtActivity extends AppCompatActivity {

    ListView lv_chat_uebaericht;
    ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_uebersicht);

        setTitle("Chats:");

        lv_chat_uebaericht = (ListView) findViewById(R.id.chats_view);

        strings = new ArrayList<String>();
        strings.add("paul");
        strings.add("carl");
        strings.add("alex");
        strings.add("nils");


        build_lv();
    }


    private void build_lv() {
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        lv_chat_uebaericht.setAdapter(arrayAdapter);

        lv_chat_uebaericht.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String chat_empfaenger = (String) lv_chat_uebaericht.getItemAtPosition(position);
                startIntent(chat_empfaenger);

            }
        });


    }


    public void startIntent(String empfaenger) {
        Intent i = new Intent(this, chatActivity.class);
        i.putExtra("EMPFAENGER", empfaenger);

        startActivity(i);
    }


}
