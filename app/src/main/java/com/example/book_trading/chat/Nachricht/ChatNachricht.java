package com.example.book_trading.chat.Nachricht;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_table")
public class ChatNachricht {

    @PrimaryKey
    @NonNull
    long id;
    @NonNull
    private String message;
    private boolean left;
    @NonNull
    private String to;


    public ChatNachricht(Boolean left, @NonNull String message, @NonNull long id, @NonNull String to) {
        this.left = left;
        this.message = message;
        this.id = id;
        this.to = to;
    }


    @NonNull
    public String getTo() {
        return to;
    }

    public void setTo(@NonNull String to) {
        this.to = to;
    }

    @NonNull
    public long getId() {
        return id;
    }


    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public String getMessage() {
        return message;
    }


    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}
