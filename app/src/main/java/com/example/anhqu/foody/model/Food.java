package com.example.anhqu.foody.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anhqu on 6/19/2017.
 */
public class Food implements Parcelable {
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

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfImage() {
        return fImage;
    }

    public void setfImage(String fImage) {
        this.fImage = fImage;
    }

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fId);
        dest.writeString(this.mId);
        dest.writeString(this.fName);
        dest.writeString(this.fImage);
        dest.writeDouble(this.fPrice);
        dest.writeString(this.fDescription);
    }

    protected Food(Parcel in) {
        this.fId = in.readString();
        this.mId = in.readString();
        this.fName = in.readString();
        this.fImage = in.readString();
        this.fPrice = in.readDouble();
        this.fDescription = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
