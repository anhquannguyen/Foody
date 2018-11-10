package com.example.anhqu.orderApp.ui.order;

import com.example.anhqu.orderApp.data.database.model.OrderItem;

import java.util.List;

public interface OrderPresenter {

    void getOrders();

    void updateOrder(OrderItem item, int position);

    void deleteOrder(OrderItem item, int position);

    void clearOrders(List<OrderItem> orderItems);

    void updateTotalPrice();
}
