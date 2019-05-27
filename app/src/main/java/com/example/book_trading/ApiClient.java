package com.example.book_trading;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://82.165.125.177/BookTrading/";//default von Android
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if(retrofit==null){
            retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
