package com.example.anhqu.foody.ui.checkout;

public interface CheckoutPresenter {

    void loadPayment();

    void placeOrder(int uId, String setTime, String location, int payment);
}
