package com.example.book_trading.app_activities;

import java.io.Serializable;

// diese Klasse speichert die Inhalte
public class ItemData implements Serializable {
    public String Name;
    public String ISBN;
    public String Zustand;
    public String Beschreibung;

    public ItemData(String name, String isbn, String zustand, String beschreibung){
        this.Name = name;
        this.ISBN = isbn;
        this.Zustand = zustand;
        this.Beschreibung = beschreibung;
    }

    public ItemData(String name){
        this.Name = name;
    }

    public String toString(){
        return Name; // hier wird der Name des Buches und der Zustand dazu zurückgegeben
    }

}