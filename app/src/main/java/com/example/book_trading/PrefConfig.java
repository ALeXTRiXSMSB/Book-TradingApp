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

    public void displayToast(String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
