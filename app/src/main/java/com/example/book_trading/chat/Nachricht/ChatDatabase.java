package com.example.book_trading.chat.Nachricht;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChatNachricht.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase INSTANCE;

    public abstract ChatNachrichtDAO getChatNachrichtDAO();

    public static ChatDatabase getChatDatabase(Context context) {
        if (INSTANCE == null) {


            INSTANCE = Room.databaseBuilder(context, ChatDatabase.class, "chatdatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
