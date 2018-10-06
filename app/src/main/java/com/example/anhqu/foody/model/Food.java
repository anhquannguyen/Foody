package com.example.anhqu.foody.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anhqu on 6/19/2017.
 */
public class Food implements Serializable {
    @SerializedName("f_id")
    public String fId;
    @SerializedName("m_id")
    public String mId;
    @SerializedName("f_name")
    public String fName;
    @SerializedName("f_image")
    public String fImage;
    @SerializedName("f_price")
    public double fPrice;
    @SerializedName("f_description")
    public String fDescription;

    public Food() {
    }

    public String getfId() {
        return fId;
    }

    public String getmId() {
        return mId;
    }

    public String getfName() {
        return fName;
    }

    public String getfImage() {
        return fImage;
    }

    public double getfPrice() {
        return fPrice;
    }

    public String getfDescription() {
        return fDescription;
    }
}
