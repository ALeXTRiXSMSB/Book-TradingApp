package com.example.book_trading;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PrefConfig {   //Loginstatus
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),Context.MODE_PRIVATE);
    }

    public void writeEmail(String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.abc.preference_email",email);
        editor.commit();
    }

    public void writeLikes(int likes){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("com.abc.preference_like",likes);
        editor.commit();
    }

    public void writeFavorites(String favorites){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.abc.preference_favorites",favorites);
        editor.commit();
    }

    public void writeDiscription(String discription){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.abc.preference_discription",discription);
        editor.commit();
    }

    public void writeLoginStatus(boolean status){       //safe user Loginstatus
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status),status);
        editor.commit();
    }

    public boolean readLoginStatus(){   //ließt den Login status aus von sharedPreferences
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status),false);
    }

    public void writeName(String name){ //schreibt username in SharePreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name),name);
        editor.commit();
    }

    public String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }

    public String readEmail(){
        return sharedPreferences.getString("com.abc.preference_email","E-Mail");
    }

    public int readLikes(){
        return sharedPreferences.getInt("com.abc.preference_likes",0);
    }

    public String readDiscription(){
        return sharedPreferences.getString("com.abc.preference_discription","Beschreibung");
    }

    public String readFavorites(){
        return sharedPreferences.getString("com.abc.preference_favorites","Favoriten");
    }


    public void displayToast(String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

}