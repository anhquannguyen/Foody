package com.example.anhqu.orderApp.data.database.model;

import com.example.anhqu.orderApp.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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


    // Payment data (Temporary)
    public List<Payment> getPayment() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(1, "MasterCard/Visa", R.drawable.ic_payment));
        payments.add(new Payment(2, "Paypal", 1));
        payments.add(new Payment(3, "Cash on delivery", 1));
        return payments;
    }
}
