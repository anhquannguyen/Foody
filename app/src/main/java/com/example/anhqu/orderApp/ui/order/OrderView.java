package com.example.anhqu.orderApp.ui.order;

import com.example.anhqu.orderApp.data.database.model.OrderItem;

import java.util.List;

public interface OrderView {

    void onloadData(List<OrderItem> orderItems);

    void onUpdateSuccess(int position);

    void onDeleteSuccess(OrderItem item, int position);

    void onClearSuccess();

    void totalPriceUpdate(double total);

    void onHandleDataFailed(String error);
}
