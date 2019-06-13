package com.example.book_trading.chat.Nachricht;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatNachrichtDAO {

    @Insert
    void insert(ChatNachricht nachricht);

    @Query("DELETE FROM chat_table")
    void deleteAll();

    @Query("SELECT * from chat_table ORDER BY id DESC")
    List<ChatNachricht> getAllChats();


}

