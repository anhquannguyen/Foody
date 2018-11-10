package com.example.anhqu.orderApp.data.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anhqu on 6/19/2017.
 */

public class Menu implements Parcelable {

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel source) {
            return new Menu(source);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
    @SerializedName("m_id")
    public String mId;
    @SerializedName("m_name")
    public String mName;
    @SerializedName("m_image")
    public String mImage;
    @SerializedName("m_description")
    public String mDescription;

    public Menu() {
    }

    protected Menu(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mImage = in.readString();
        this.mDescription = in.readString();
    }

    public String getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }


    public String getmImage() {
        return mImage;
    }


    public String getmDescription() {
        return mDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mImage);
        dest.writeString(this.mDescription);
    }
}
