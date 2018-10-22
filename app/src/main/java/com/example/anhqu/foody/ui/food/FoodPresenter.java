package com.example.anhqu.foody.ui.food;

import com.example.anhqu.foody.data.database.model.OrderItem;

public interface FoodPresenter {
    void getFoods(String id);

    void getCount();

    void addOrder(OrderItem item);

    void deleteOrder(OrderItem item);
}
