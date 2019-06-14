package com.example.book_trading;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editTextInfo, editTextMail, editTextBuch;

    ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Bearbeiten")
                .setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {   //bei cancel Dialogfenster schließen
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {    //Beim klicken auf ok die Inhalte übernehmen
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String info = editTextInfo.getText().toString();
                        String mail = editTextMail.getText().toString();
                        String buch = editTextBuch.getText().toString();
                        listener.applyTexts(info,mail,buch);
                    }
                });
        editTextInfo= view.findViewById(R.id.Info);
        editTextMail = view.findViewById(R.id.Mail);
        editTextBuch = view.findViewById(R.id.Buch);
        return builder.create();
    }

    public interface  ExampleDialogListener{
        void applyTexts(String info, String mail, String buch);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement Example Dialog");
        }
    }

}