package com.example.anhqu.foody.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anhqu on 10/1/2018.
 */

public class Payment implements Serializable {
    @SerializedName("pId")
    private int pId;
    @SerializedName("pName")
    private String pName;
    @SerializedName("pIcon")
    private int pIcon;

    public Payment() {
    }

    public Payment(int pId, String pName, int pIcon) {
        this.pId = pId;
        this.pName = pName;
        this.pIcon = pIcon;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpIcon() {
        return pIcon;
    }

    public void setpIcon(int pIcon) {
        this.pIcon = pIcon;
    }
}
