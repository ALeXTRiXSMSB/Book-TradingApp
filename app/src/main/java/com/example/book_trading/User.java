package com.example.book_trading;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("response")
    private String Response;

    @SerializedName("u_id")
    private int u_id;

    @SerializedName("u_name")
    private String u_name;

    @SerializedName("u_email")
    private String u_email;

    @SerializedName("u_discription")
    private String u_discription;

    @SerializedName("u_like")
    private int u_like;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
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

    public int getU_like() {
        return u_like;
    }

    public void setU_like(int u_like) {
        this.u_like = u_like;
    }
}