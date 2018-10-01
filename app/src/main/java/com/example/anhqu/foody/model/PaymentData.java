package com.example.anhqu.foody.model;

import com.example.anhqu.foody.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhqu on 10/1/2018.
 */

public class PaymentData {

    public List<Payment> getPayment() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(1, "Mastercard or Visa", R.drawable.ic_payment));
        payments.add(new Payment(2, "Paypal", R.drawable.ic_paypal));
        return payments;
    }
}
