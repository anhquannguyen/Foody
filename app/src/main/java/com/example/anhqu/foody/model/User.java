package com.example.anhqu.foody.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("user_id")
    private int uId;
    @SerializedName("user_name")
    private String uName;
    @SerializedName("user_pw")
    private String uPw;
    @SerializedName("user_fullname")
    private String uFullName;
    @SerializedName("user_mobile")
    private String uMobile;

    public User(int uId, String uName, String uPw, String uFullName, String uMobile) {
        this.uId = uId;
        this.uName = uName;
        this.uPw = uPw;
        this.uFullName = uFullName;
        this.uMobile = uMobile;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPw() {
        return uPw;
    }

    public void setuPw(String uPw) {
        this.uPw = uPw;
    }

    public String getuFullName() {
        return uFullName;
    }

    public void setuFullName(String uFullName) {
        this.uFullName = uFullName;
    }

    public String getuMobile() {
        return uMobile;
    }

    public void setuMobile(String uMobile) {
        this.uMobile = uMobile;
    }
}
