package com.example.book_trading.app_activities;

import java.io.Serializable;

//Diese klasse speichert die Inhalte kann später ggf. gelöscht werden
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
        return Name + " (" + Zustand +")"; //Hier wird der Name des Buches und der Zustand dazu zurück gegeben
    }

}