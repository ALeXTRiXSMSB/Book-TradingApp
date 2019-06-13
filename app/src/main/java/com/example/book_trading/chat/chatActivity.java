package com.example.book_trading.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.book_trading.R;
import com.example.book_trading.chat.Nachricht.ChatDatabase;
import com.example.book_trading.chat.Nachricht.ChatNachricht;
import com.example.book_trading.chat.Nachricht.ChatNachrichtDAO;
import com.example.book_trading.chat.Nachricht.NachrichtArrayAdapter;

import java.util.Collections;
import java.util.List;

//TODO kommentieren der Klassen
public class chatActivity extends AppCompatActivity {
    private String message;

    private String empfaenger;

    private ChatDatabase chatDatabase;
    private ChatNachrichtDAO chatNachrichtDAO;

    private BroadcastReceiver broadcastReceiver;
    private chatBroadcastReceiver receiver;

    public static Boolean activity_active;


    ImageButton send;
    EditText msg;

    NachrichtArrayAdapter chatArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_active = true;
        /**empfaenger wird übergeben*/
        Bundle b = getIntent().getExtras();

        empfaenger = b.getString("EMPFAENGER");
        setTitle("Chat mit: " + empfaenger);
        setContentView(R.layout.activity_chat);
        send = (ImageButton) findViewById(R.id.btn_send);
        msg = (EditText) findViewById(R.id.editText);


        build_Database();

        registerReceiver(this);
        registerReceiver_empfangen();


        update_array_adapter();

        button_pressed();


    }


    public void build_Database() {


        chatDatabase = ChatDatabase.getChatDatabase(getApplicationContext());

        chatNachrichtDAO = chatDatabase.getChatNachrichtDAO();

    }


    public void buildChatArrayAdapter() {
        chatArrayAdapter = new NachrichtArrayAdapter(getApplicationContext(), R.layout.item_send);
        final ListView listView = (ListView) findViewById(R.id.messages_view);
        listView.setAdapter(chatArrayAdapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }


    public void button_pressed() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = msg.getText().toString();
                message = message.trim();
                if (!(message.equals(""))) {
                    sendMessage(message);
                }
                msg.getText().clear();


            }
        });
    }

    /**
     * ID ist die aktuelle zeit in millisekunden
     */
    public long getID() {
        long id = System.currentTimeMillis();


        return id;
    }

    //TODO: Chat Nachricht benötigt für Datenbankabspeicherung: den Empfänger und Sender für Zuordnung
    private boolean receiveMessage(String s) {


        /**ArrayAdapter wird geupdatet*/
        update_array_adapter();
        return true;
    }


    private boolean sendMessage(String message) {


        /**datenbank wird aktualisiert*/
        ChatNachricht nachricht = new ChatNachricht(true, message, getID(), empfaenger);
        nachricht.setLeft(true);
        nachricht.setMessage(message);
        nachricht.setTo(empfaenger);


        chatNachrichtDAO.insert(nachricht);
/**--------------------------------*/

        /**ArrayAdapter wird geupdatet*/
        update_array_adapter();

        /**Broadcast wenn nachricht geschickt wird*/
        Intent intent = new Intent(chatBroadcastReceiver.BC_ACTION_GESENDET);
        intent.putExtra("EMPFAENGER", empfaenger);
        intent.putExtra("GESENDET", message);
        sendBroadcast(intent);


        return true;
    }


    /**
     * aus Datenbank wird die Liste der Nachrichten geholt und in den ArrayAdapter übertragen
     */
    private void update_array_adapter() {
        buildChatArrayAdapter();
        List<ChatNachricht> items = chatNachrichtDAO.getAllChats();
        Collections.reverse(items);
        for (ChatNachricht i : items) {
            if (i.getTo().equals(empfaenger)) {
                chatArrayAdapter.add(i);
            }
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------------------
     */

    private void registerReceiver(Context context) {
        receiver = new chatBroadcastReceiver();
        IntentFilter filter = new IntentFilter(chatBroadcastReceiver.BC_ACTION_GESENDET);
        context.registerReceiver(receiver, filter);
    }

    private void registerReceiver_empfangen() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(chatBroadcastReceiver.BC_INTENT_EMPFANGEN)) {
                    Bundle b = intent.getExtras();
                    String s = b.getString("MESSAGE");
                    receiveMessage(s);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(chatBroadcastReceiver.BC_INTENT_EMPFANGEN));
    }

    @Override
    public void onResume() {
        activity_active = true;
        super.onResume();
    }

    public void onPause() {
        update_array_adapter();
        activity_active = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(getApplicationContext(), xmppService.class));


        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(receiver);

        activity_active = false;
        super.onDestroy();


    }

}