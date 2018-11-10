package com.example.anhqu.orderApp.ui.checkout;

public interface CheckoutPresenter {

    void loadPayment();

    void placeOrder(int uId, String setTime, String location, int payment);
}
