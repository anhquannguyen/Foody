package com.example.anhqu.foody.ui.order;

import com.example.anhqu.foody.data.database.model.OrderItem;

import java.util.List;

public interface OrderPresenter {

    void getOrders();

    void updateOrder(OrderItem item, int position);

    void deleteOrder(OrderItem item, int position);

    void clearOrders(List<OrderItem> orderItems);

    void updateTotalPrice();
}
