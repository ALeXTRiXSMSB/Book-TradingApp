package com.example.book_trading.datenbank;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.book_trading.R;

public class PrefConfig {   //Loginstatus
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeAllreadyLiked(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(readAlleadyLiked()).append(",").append(name);
        editor.putString(context.getString(R.string.pref_allready_liked),sb.toString());
        editor.commit();
    }

    public String readAlleadyLiked(){
        return sharedPreferences.getString(context.getString(R.string.pref_allready_liked),"0");
    }

    public void writeUID(String u_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_u_id), u_id);
        editor.commit();
    }

    public String readUID() {
        return sharedPreferences.getString(context.getString(R.string.pref_u_id), "0");
    }

    public void writeEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_email), email);
        editor.commit();
    }

    public void writeLikes(String likes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_likes), likes);
        editor.commit();
    }

    public void writeFavorites(String favorites) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_favorites), favorites);
        editor.commit();
    }

    public void writeDiscription(String discription) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_discription), discription);
        editor.commit();
    }

    public void writeLoginStatus(boolean status) {       //safe user Loginstatus
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.commit();
    }

    public boolean readLoginStatus() {   //ließt den Login status aus von sharedPreferences
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writeName(String name) { //schreibt username in SharePreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name), name);
        editor.commit();
    }

    public void writePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_u_password), password);
        editor.commit();


    }

    public String readName() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_name), "User");
    }

    public String readEmail() {
        return sharedPreferences.getString(context.getString(R.string.pref_email), "E-Mail");
    }

    public String readLikes() {
        return sharedPreferences.getString(context.getString(R.string.pref_likes), "0");
    }

    public String readDiscription() {
        return sharedPreferences.getString(context.getString(R.string.pref_discription), "Beschreibung");
    }

    public String readFavorites() {
        return sharedPreferences.getString(context.getString(R.string.pref_favorites), "Favoriten");
    }

    public String readPassword() {
        return sharedPreferences.getString(context.getString(R.string.pref_u_password), "0");
    }

    public void displayToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}