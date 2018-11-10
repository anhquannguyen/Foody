package com.example.anhqu.orderApp.ui.checkout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.data.database.model.Payment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhqu on 10/1/2018.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {
    private List<Payment> payments;
    private Context context;


    public PaymentAdapter(List<Payment> payments, Context context) {
        this.payments = payments;
        this.context = context;
    }

    @Override
    public PaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_payment,parent,false);
        return new PaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentHolder holder, int position) {
        Payment payment = payments.get(position);
        Glide.with(context).load(payment.getpIcon()).into(holder.iconPayment);
        holder.txtPayment.setText(payment.getpName());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.payment_icon)
        ImageView iconPayment;
        @BindView(R.id.txt_payment)
        TextView txtPayment;

        public PaymentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
