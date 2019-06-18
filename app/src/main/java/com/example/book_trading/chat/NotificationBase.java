package com.example.book_trading.chat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class NotificationBase extends Application {

    public static final String CHANNEL_CHAT_ID = "chatnotification_booktrading";

    @Override
    public void onCreate() {
        createNotificationChannels();

        super.onCreate();
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chatnote = new NotificationChannel(CHANNEL_CHAT_ID, "ChatChanel", NotificationManager.IMPORTANCE_HIGH);
            chatnote.setDescription("This Channel is used for Booktrading Chat");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chatnote);
        }
    }


}

