package com.example.book_trading.datenbank;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("register.php")
    Call<User> performRegistration(@Query("u_name") String UserName,
                                   @Query("u_pass") String UserPassword);

    @GET("login.php")
    Call<User> performUserLogin(@Query("u_name") String UserName,
                                @Query("u_pass") String UserPassword);

    @GET("getThreads.php")
    Call<List<Thread>> performGetThreads();

    @GET("ownThreads.php")
    Call<List<Thread>> performGetOwnThreads(@Query("u_name") String username);

    @GET("getThread.php")
    Call<Thread> performGetThread(@Query("t_id") String t_id);

    @GET("getProfile.php")
    Call<User> performGetProfile(@Query("u_name") String name);

    //info mail buch
    @GET("editProfile.php")
    Call<User> performEditProfile(@Query("u_email") String email,
                                  @Query("u_discription") String beschreibung,
                                  @Query("u_favorites") String favoriten,
                                  @Query("u_name") String u_id);

    @GET("createThread.php")
    Call<Thread> performCreateThread(@Query("t_titel") String titel,
                                     @Query("t_discription") String beschreibung,
                                     @Query("t_isbn") String isbn,
                                     @Query("t_zustand") String zustand,
                                     @Query("u_name") String u_name);

    @GET("anzahlThread.php")
    Call<String> performCount(@Query("u_name") String u_name);

    @GET("incLike.php")
    Call<Integer> performinclike(@Query("u_name") String u_name);

    @GET("deleteThread.php")
    Call<Thread> performDeleteThread(@Query("t_id") String t_name);
}