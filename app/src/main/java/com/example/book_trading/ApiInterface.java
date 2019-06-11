package com.example.book_trading;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("register.php")
    Call<User> performRegistration(@Query("u_name") String UserName,@Query("u_pass") String UserPassword);

    @GET("login.php")
    Call<User> performUserLogin(@Query("u_name") String UserName,
                                @Query("u_pass") String UserPassword);

    @GET("getThreads.php")
    Call<ArrayList<Thread>> performGetThreads(@Query("t_id") int t_id,
                                              @Query("t_titel") String titel,
                                              @Query("t_discription") String beschreibung);

    @GET("getThread.php")
    Call<Thread> performGetThread(@Query("t_id") int t_id,
                                  @Query("t_titel") String t_name,
                                  @Query("t_discription") String beschreibung,
                                  @Query("u_name") String name);

    @GET("getProfile.php")
    Call<User> performGetProfile(@Query("u_id") int u_id,
                                 @Query("u_name") String name,
                                 @Query("u_email") String email,
                                 @Query("u_discription") String beschreibung,
                                 @Query("u_favorites") String favorites,
                                 @Query("u_like") int likes);

    //info mail buch
    @GET("editProfile.php")
    Call<User> performEditProfile(@Query("u_email") String email,
                                  @Query("u_discription") String beschreibung,
                                  @Query("u_favorites") String favoriten,
                                  @Query("u_name") String u_id);

    @GET("createThread.php")
    Call<Thread> performCreateThread(@Query("t_titel") String titel,
                                     @Query("t_discription") String beschreibung,
                                     @Query("u_id") int u_id);
}