package com.example.book_trading.chat.Nachricht;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.book_trading.R;

import org.jivesoftware.smack.chat2.Chat;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class uebersichtArrayAdapter extends ArrayAdapter<String> {

    private TextView chatText;
    private List<String> empfaengerList = new ArrayList<String>();
    private Context context;
    private TextView letzte_nachricht;
    private ChatDatabase chatDatabase;
    private ChatNachrichtDAO chatNachrichtDAO;


    public uebersichtArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        build_Database(context);
    }

    @Override
    public void add(String empfaenger) {
        empfaengerList.add(empfaenger);
        super.add(empfaenger);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        String empfaengerObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.item_empfaenger, parent, false);


        chatText = (TextView) row.findViewById(R.id.item_empfaenger);


        List<ChatNachricht> chatNachrichts = chatNachrichtDAO.getAllChats();
        ArrayList<ChatNachricht> chatNachrichts1 = new ArrayList<ChatNachricht>();
        chatNachrichts1.addAll(chatNachrichts);
        ChatNachricht chatnachricht = chatNachrichts1.get(chatNachrichts1.size() - 1);


        chatText.setText(empfaengerObj + "\n");


        return row;
    }

    private void build_Database(Context context) {


        chatDatabase = ChatDatabase.getChatDatabase(context.getApplicationContext());

        chatNachrichtDAO = chatDatabase.getChatNachrichtDAO();

    }


}
