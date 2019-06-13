package com.example.book_trading.chat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.book_trading.LoginActivity;
import com.example.book_trading.R;

public class chatBroadcastReceiver extends BroadcastReceiver {

    //TODO bessere bezeichnungen finden
    public static final String BC_ACTION_EMPFANGEN = "EMPFANGEN";
    public static final String BC_ACTION_GESENDET = "GESENDET";
    public static final String BC_INTENT_EMPFANGEN = "EMPFANGEN_MAIN";
    public static final String BC_INTENT_GESENDET = "GESENDET_SERVICE";
    public static final String BC_LOGIN_GESENDET = "GESENDET_LOGIN_SERVICE";


    private NotificationManagerCompat notificationManager;
    private Intent login_start_XMPP;


    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);

        /**Action die von der Activity kommt*/
        if (intent.getAction().equals(BC_ACTION_GESENDET)) {

            Bundle b = intent.getExtras();
            String msg_gesendet = b.getString("GESENDET");
            String to = b.getString("EMPFAENGER");

            Log.d("An " + to + ", nachricht gesendet:", msg_gesendet);


            /**Broadcast schickt intent weiter zu Service*/
            Intent in = new Intent(BC_INTENT_GESENDET);
            in.putExtra("MESSAGE", msg_gesendet);
            in.putExtra("EMPFAENGER", to);
            context.sendBroadcast(in);


        }

        /**Action die von dem XMPPService kommt*/
        if (intent.getAction().equals(BC_ACTION_EMPFANGEN)) {


            Bundle bundle = intent.getExtras();
            String s = bundle.getString("EMPFANGEN");
            String sender = bundle.getString("SENDER");
            Log.d("BroadcastReceiver", s);
            /**es wird nur notification gesendet wenn activity nicht aktiv ist*/
            if (chatActivity.activity_active != null) {
                if (!(chatActivity.activity_active)) {
                    sendNotification(s, context, sender);
                }
            }

            /**Broadcast schickt intent weiter zur mainactivity*/
            Intent in = new Intent(BC_INTENT_EMPFANGEN);
            in.putExtra("MESSAGE", s);
            context.sendBroadcast(in);
        }

        if (intent.getAction().equals(BC_LOGIN_GESENDET)) {

            Bundle bundle = intent.getExtras();
            String password = bundle.getString("PASSWORD");
            String username = bundle.getString("USERNAME");
            Boolean account_exists = bundle.getBoolean("ACCOUNT_EXIST");


            startXmpp(username, password, account_exists, context);
        }


    }


    private void sendNotification(String notificationmessage, Context context, String sender) {


        //Intent resultIntent = new Intent(context, MainActivity.class);
        //resultIntent.putExtra("EMPFAENGER", sender);

        Intent resultIntent = new Intent(context, LoginActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(context, NotificationBase.CHANNEL_CHAT_ID)
                .setSmallIcon(R.drawable.ic_chat_note)
                .setContentTitle("Nachricht von: " + sender)
                .setContentText(notificationmessage)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


    private void startXmpp(String USERNAME, String PASSWORD, Boolean ACCOUNT_EXIST, Context context) {
        login_start_XMPP = new Intent(context, xmppService.class);


/**account exist ist wichtig um abzuschätzen ob registriert oder angemeldet wird*/
        login_start_XMPP.putExtra("USERNAME", USERNAME);
        login_start_XMPP.putExtra("PASSWORD", PASSWORD);
        login_start_XMPP.putExtra("ACCOUNT_EXIST", ACCOUNT_EXIST);
        context.startService(login_start_XMPP);

    }


}

