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

import java.util.ArrayList;
import java.util.List;

public class uebersichtArrayAdapter extends ArrayAdapter<String> {

    private TextView chatText;
    private List<String> empfaengerList = new ArrayList<String>();
    private Context context;


    public uebersichtArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
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
        chatText.setText(empfaengerObj);
        return row;
    }


}
