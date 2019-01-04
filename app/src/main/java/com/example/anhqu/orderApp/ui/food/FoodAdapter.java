package com.example.anhqu.orderApp.ui.food;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.data.database.model.Food;
import com.example.anhqu.orderApp.data.database.model.OrderItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhqu on 6/17/2018.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<OrderItem> itemList;
    private Context context;
    private onClickInterface anInterface;

    public FoodAdapter(List<OrderItem> itemList, Context context) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setAnInterface(onClickInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, final int position) {
        OrderItem item = itemList.get(position);
        Food object = item.getFood();
        Glide.with(context)
                .load(object.getfImage())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.image);
        holder.textName.setText(object.getfName());
        holder.textPrice.setText(String.format("$ %s", object.getfPrice()));
        holder.btnDelete.setVisibility(View.INVISIBLE);

        // quantity = 0, hiding text
        if (item.getQuantity() == 0) {
            holder.textQuant.setVisibility(View.INVISIBLE);
        } else {

            // quantity != 0, unhidden text if it's hidden before
            if (holder.textQuant.getVisibility() == View.INVISIBLE) {
                holder.textQuant.setVisibility(View.VISIBLE);
            }
            holder.textQuant.setText(String.format("x%s", item.getQuantity()));
        }

        onClickListener(item, holder);
    }

    private void onClickListener(OrderItem item, final FoodViewHolder holder) {
        holder.foodRow.setOnClickListener(view -> {
            item.setQuantity(item.getQuantity() + 1);
            item.setTotalPrice(item.getQuantity() * item.getFood().getfPrice());
            holder.textQuant.setText(String.format("x%s", item.getQuantity()));
            if (holder.textQuant.getVisibility() == View.INVISIBLE) {
                holder.textQuant.setVisibility(View.VISIBLE);
            }
            if (context instanceof FoodActivity) {
                ((FoodActivity) context).onAppear(holder.textQuant);
            }
            if (anInterface != null) {
                anInterface.onClick(item);
            }
        });

        holder.foodRow.setOnLongClickListener((View view) -> {

            if (holder.textQuant.getVisibility() == View.VISIBLE) {
                holder.textQuant.setVisibility(View.INVISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.foodRow.setClickable(false);

                if (context instanceof FoodActivity) {
                    ((FoodActivity) context).onAppear(holder.btnDelete);
                }

                holder.btnDelete.setOnClickListener(v -> {
                    if (anInterface != null) {
                        anInterface.onLongClick(item);
                        holder.btnDelete.setVisibility(View.INVISIBLE);
                    }
                });
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface onClickInterface {
        void onClick(OrderItem item);

        void onLongClick(OrderItem item);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_row)
        ConstraintLayout foodRow;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_price)
        TextView textPrice;
        @BindView(R.id.food_img)
        ImageView image;
        @BindView(R.id.text_quantity)
        TextView textQuant;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
