package com.example.book_trading.datenbank;

import android.content.Context;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySQLDatabaseHelper {
    private static PrefConfig prefConfig;
    private static ApiInterface apiInterface;

    public MySQLDatabaseHelper(Context _context){
        prefConfig = new PrefConfig(_context);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    //CRUDs
    //CREATE
    public void register(String username, String passwort){
        Call<User> call = this.apiInterface.performRegistration(username,passwort);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch(response.body().getResponse()){
                    case "success":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Registrierung Erfolgt");
                        break;
                    }
                    case "user exists":{
                        MySQLDatabaseHelper.prefConfig.displayToast("User Existstiert bereits");
                        break;
                    }
                    case "missing argument":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Bitte beide Felder ausfüllen");
                        break;
                    }
                    case "wrong request type":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Was ist denn da Passiert?");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public void createThread(String titel, String beschreibung, int userid){
        Call<Thread> call = this.apiInterface.performCreateThread(titel,beschreibung,userid);
        call.enqueue(new Callback<Thread>() {
            @Override
            public void onResponse(Call<Thread> call, Response<Thread> response) {
                switch(response.body().getResponse()){
                    case "success":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Thread Erstellt");
                        break;
                    }
                    case "thread exists":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Thread Existstiert bereits");
                        break;
                    }
                    case "missing argument":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Bitte alle Felder ausfüllen");
                        break;
                    }
                    case "wrong request type":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Was ist denn da Passiert?");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<Thread> call, Throwable t) {

            }
        });
    }

    //READ
    public void login(String username, String passwort){
        Call<User> call = this.apiInterface.performUserLogin(username,passwort);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {//Prüfen des Status beim einloggen
                switch(response.body().getResponse()){
                    case "success":{
                        MySQLDatabaseHelper.prefConfig.writeUID(response.body().getU_id());
                        MySQLDatabaseHelper.prefConfig.writeName(response.body().getU_name());
                        MySQLDatabaseHelper.prefConfig.writeEmail(response.body().getU_email());
                        MySQLDatabaseHelper.prefConfig.writeDiscription(response.body().getU_discription());
                        MySQLDatabaseHelper.prefConfig.writeFavorites(response.body().getU_favorites());
                        MySQLDatabaseHelper.prefConfig.writeLikes(response.body().getU_like());
                        MySQLDatabaseHelper.prefConfig.writeLoginStatus(true); //LoginStatus auf true setzen um sich ein zu loggen
                        break;
                    }
                    case "no data":{
                        MySQLDatabaseHelper.prefConfig.displayToast("User Existstiert nicht");
                        break;
                    }
                    case "missing argument":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Bitte alle Felder ausfüllen");
                        break;
                    }
                    case "wrong request type":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Was ist denn da Passiert?");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public void getOwnProfileUpdate(String username, String passwort){
        Call<User> call = this.apiInterface.performUserLogin(username,passwort);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {//Prüfen des Status beim einloggen
                switch(response.body().getResponse()){
                    case "success":{
                        MySQLDatabaseHelper.prefConfig.writeUID(response.body().getU_id());
                        MySQLDatabaseHelper.prefConfig.writeName(response.body().getU_name());
                        MySQLDatabaseHelper.prefConfig.writeEmail(response.body().getU_email());
                        MySQLDatabaseHelper.prefConfig.writeDiscription(response.body().getU_discription());
                        MySQLDatabaseHelper.prefConfig.writeFavorites(response.body().getU_favorites());
                        MySQLDatabaseHelper.prefConfig.writeLikes(response.body().getU_like());
                        break;
                    }
                    case "no data":{
                        MySQLDatabaseHelper.prefConfig.displayToast("User Existstiert nicht");
                        break;
                    }
                    case "missing argument":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Bitte alle Felder ausfüllen");
                        break;
                    }
                    case "wrong request type":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Was ist denn da Passiert?");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public void getStrangerProfile(String u_name){
        Call<User> call = this.apiInterface.performGetProfile(u_name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {//Prüfen des Status beim einloggen
                switch(response.body().getResponse()){
                    case "success":{
                        User tmp = new User();
                        tmp.setU_id(response.body().getU_id());
                        tmp.setU_name(response.body().getU_name());
                        tmp.setU_email(response.body().getU_email());
                        tmp.setU_discription(response.body().getU_discription());
                        tmp.setU_favorites(response.body().getU_favorites());
                        tmp.setU_like(response.body().getU_like());
                        break;
                    }
                    case "no data":{
                        MySQLDatabaseHelper.prefConfig.displayToast("User Existstiert nicht");
                        break;
                    }
                    case "missing argument":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Bitte alle Felder ausfüllen");
                        break;
                    }
                    case "wrong request type":{
                        MySQLDatabaseHelper.prefConfig.displayToast("Was ist denn da Passiert?");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public void getOwnThreads(){

    }

    public void getAllThreads(){
        Call<List<Thread>> call = this.apiInterface.performGetThreads();
        call.enqueue(new Callback<List<Thread>>() {
            @Override
            public void onResponse(Call<List<Thread>> call, Response<List<Thread>> response) {
                List<Thread> tmp;
            }

            @Override
            public void onFailure(Call<List<Thread>> call, Throwable t) {
            }
        });
    }

    public void searchThreads(){

    }

    //UPDATE
    public void editProfile(){

    }

    public void updateThread(){

    }

    //DELETE
    public void deleteThread(){

    }

    public void writeToSharedPreference(){

    }

}