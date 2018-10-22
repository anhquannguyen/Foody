package com.example.anhqu.foody.data.database.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("order_id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("set_time")
    private String setTime;
    @SerializedName("order_location")
    private String location;
    @SerializedName("order_payment")
    private int payment;
    @SerializedName("order_status")
    private int status;
    @SerializedName("time_place")
    private String timePlace;
    @SerializedName("list_item")
    private List<OrderItem> orderItems;

    public Order(int id, int userId, String setTime, String location, int payment, int status, String timePlace, List<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.setTime = setTime;
        this.location = location;
        this.payment = payment;
        this.status = status;
        this.timePlace = timePlace;
        this.orderItems = orderItems;
    }

    public Order(int userId, String setTime, String location, int payment, List<OrderItem> orderItems) {
        this.userId = userId;
        this.setTime = setTime;
        this.location = location;
        this.payment = payment;
        this.orderItems = orderItems;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getSetTime() {
        return setTime;
    }

    public String getLocation() {
        return location;
    }

    public int getPayment() {
        return payment;
    }

    public int getStatus() {
        return status;
    }

    public String getTimePlace() {
        return timePlace;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
