package com.example.anhqu.foody.ui.food;

import com.example.anhqu.foody.data.database.model.OrderItem;

import java.util.List;

public interface FoodView {
    void onLoadSuccess(List<OrderItem> itemList);

    void onLoadFailed();

    void onLoadError(String error);

    void onOrderVisible();

    void onOrderInvisible();

    void onViewRemoved(OrderItem item);
}
