package com.example.book_trading;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("response")
    private String response;

    @SerializedName("u_id")
    private int u_id;

    @SerializedName("u_name")
    private String u_name;

    @SerializedName("u_email")
    private String u_email;

    @SerializedName("u_discription")
    private String u_discription;

    @SerializedName("u_favorites")
    private String u_favorites;

    @SerializedName("u_like")
    private int u_like;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_discription() {
        return u_discription;
    }

    public void setU_discription(String u_discription) {
        this.u_discription = u_discription;
    }

    public String getU_favorites() {
        return u_favorites;
    }

    public void setU_favorites(String u_favorites) {
        this.u_favorites = u_favorites;
    }

    public int getU_like() {
        return u_like;
    }

    public void setU_like(int u_like) {
        this.u_like = u_like;
    }

}