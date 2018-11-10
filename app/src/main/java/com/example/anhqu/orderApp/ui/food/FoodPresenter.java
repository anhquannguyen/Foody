package com.example.anhqu.orderApp.ui.food;

import com.example.anhqu.orderApp.data.database.model.OrderItem;

public interface FoodPresenter {
    void getFoods(String id);

    void getCount();

    void addOrder(OrderItem item);

    void deleteOrder(OrderItem item);
}
