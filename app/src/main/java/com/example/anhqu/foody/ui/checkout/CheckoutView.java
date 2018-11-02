package com.example.anhqu.foody.ui.checkout;

import com.example.anhqu.foody.data.database.model.Payment;

import java.util.List;

public interface CheckoutView {
    void onPlaceSucess();

    void onPlaceFailed();

    void onLoadPayment(List<Payment> payments);

    void onError(String msg);
}
