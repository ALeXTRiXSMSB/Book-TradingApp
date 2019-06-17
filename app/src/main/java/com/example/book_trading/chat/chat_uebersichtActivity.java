package com.example.book_trading.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.book_trading.R;
import com.example.book_trading.chat.Nachricht.ChatDatabase;
import com.example.book_trading.chat.Nachricht.ChatNachricht;
import com.example.book_trading.chat.Nachricht.ChatNachrichtDAO;
import com.example.book_trading.chat.Nachricht.NachrichtArrayAdapter;
import com.example.book_trading.chat.Nachricht.uebersichtArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class chat_uebersichtActivity extends AppCompatActivity {

    private ListView lv_chat_uebaericht;

    private ChatDatabase chatDatabase;
    private ChatNachrichtDAO chatNachrichtDAO;
    private uebersichtArrayAdapter uebersicht;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_uebersicht);

        setTitle("Chats:");

        build_Database();


        lv_chat_uebaericht = (ListView) findViewById(R.id.chats_view);

        buildChatArrayAdapter();

        EmpfängertoArrayAdadpter();


        build_lv();

    }


    private void build_lv() {

        lv_chat_uebaericht.setAdapter(uebersicht);

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

    private void EmpfängertoArrayAdadpter() {
        List<String> alleEmpfänger = chatNachrichtDAO.getAllEmpfänger();
        for (String empfänger : alleEmpfänger) {
            uebersicht.add(empfänger);
        }

    }


    public void buildChatArrayAdapter() {
        uebersicht = new uebersichtArrayAdapter(getApplicationContext(), R.layout.item_empfaenger);
        final ListView listView = (ListView) findViewById(R.id.chats_view);
        listView.setAdapter(uebersicht);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(uebersicht);

        uebersicht.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(uebersicht.getCount() - 1);
            }
        });
    }


    private void build_Database() {


        chatDatabase = ChatDatabase.getChatDatabase(getApplicationContext());

        chatNachrichtDAO = chatDatabase.getChatNachrichtDAO();

    }

    public String id_to_time() {
        String letzte_nachricht = "Unbekannt...";
        List<ChatNachricht> alleids = chatNachrichtDAO.getAllChats();

        for (ChatNachricht id : alleids) {
            Long id_long = (id.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(id_long);
            letzte_nachricht = resultdate.toString();
        }


        return letzte_nachricht;
    }


}