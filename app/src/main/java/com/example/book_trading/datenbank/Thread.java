package com.example.book_trading.datenbank;

import com.google.gson.annotations.SerializedName;

public class Thread {

    @SerializedName("response")
    private String response;

    @SerializedName("t_id")
    private int t_id;

    @SerializedName("t_titel")
    private String t_titel;

    @SerializedName("t_discription")
    private String t_discription;

    @SerializedName("u_name")
    private String u_name;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getT_titel() {
        return t_titel;
    }

    public void setT_titel(String t_titel) {
        this.t_titel = t_titel;
    }

    public String getT_discription() {
        return t_discription;
    }

    public void setT_discription(String t_discription) {
        this.t_discription = t_discription;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

}