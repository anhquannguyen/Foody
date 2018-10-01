package com.example.anhqu.foody.ui.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.model.Food;
import com.example.anhqu.foody.model.OrderItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhqu on 7/5/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.CartItemHolder> {
    private List<OrderItem> orderItems;
    private Context context;
    private onClickInterface anInterface;

    public OrderAdapter(List<OrderItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    public void setAnInterface(onClickInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_in_order, parent, false);
        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemHolder holder, int position) {
        final OrderItem item = orderItems.get(position);
        Food object = item.getFood();
        holder.textName.setText(object.getfName());
        holder.textPrice.setText(String.format("%s $", item.getTotalPrice()));
        holder.textQuant.setText(String.format("%sx", item.getQuantity()));
        holder.layoutRow.setOnClickListener(view -> {
            int h = holder.layoutRow.getHeight();
            if (anInterface != null) {
                anInterface.onClick(position, h);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();

    }

    public interface onClickInterface {
        void onClick(int pos, int h);
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView textName;
        @BindView(R.id.txt_price)
        TextView textPrice;
        @BindView(R.id.txt_quant)
        TextView textQuant;
        @BindView(R.id.item_row)
        ConstraintLayout layoutRow;

        public CartItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
