package com.example.anhqu.orderApp.data.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anhqu on 7/1/2018.
 */
@Entity(tableName = "OrderItem")
public class OrderItem implements Parcelable {
    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "o_id")
    private int id;
    @ColumnInfo(name = "order_id")
    private int placedOrderId;
    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "total_price")
    private double totalPrice;
    @Embedded
    private Food food;

    public OrderItem(Food food) {
        this.food = food;
    }

    protected OrderItem(Parcel in) {
        this.id = in.readInt();
        this.placedOrderId = in.readInt();
        this.food = in.readParcelable(Food.class.getClassLoader());
        this.quantity = in.readInt();
        this.totalPrice = in.readDouble();
    }

    public int getPlacedOrderId() {
        return placedOrderId;
    }

    public void setPlacedOrderId(int placedOrderId) {
        this.placedOrderId = placedOrderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.placedOrderId);
        dest.writeParcelable((Parcelable) this.food, flags);
        dest.writeInt(this.quantity);
        dest.writeDouble(this.totalPrice);
    }
}
