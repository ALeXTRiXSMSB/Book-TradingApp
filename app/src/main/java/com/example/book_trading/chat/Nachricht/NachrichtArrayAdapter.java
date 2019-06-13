package com.example.book_trading.chat.Nachricht;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.book_trading.R;

import java.util.ArrayList;
import java.util.List;

public class NachrichtArrayAdapter extends ArrayAdapter<ChatNachricht> {
    private TextView chatText;
    private List<ChatNachricht> chatMessageList = new ArrayList<ChatNachricht>();
    private Context context;

    @Override
    public void add(ChatNachricht nachricht) {
        chatMessageList.add(nachricht);
        super.add(nachricht);
    }

    public NachrichtArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatNachricht getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatNachricht chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.isLeft()) {
            row = inflater.inflate(R.layout.item_send, parent, false);
        } else {
            row = inflater.inflate(R.layout.item_receive, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.getMessage());
        return row;
    }
}

