package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.SessionManager;
import com.example.anhqu.foody.model.OrderItem;
import com.example.anhqu.foody.model.localDb.DaoRepository;
import com.example.anhqu.foody.ui.adapter.OrderAdapter;
import com.example.anhqu.foody.ui.fragment.SheetOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by anhqu on 7/5/2018.
 */

public class OrderActivity extends BaseActivity implements OrderAdapter.onClickInterface, SheetOrderFragment.EditOrderInterface {
    private static final String KEY_DATA = "order_data";
    private static final String KEY_POS = "order_position";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cart_bottom_sheet)
    ConstraintLayout cartBottomSheet;
    @BindView(R.id.txt_total_price)
    TextView txtPrice;
    @BindView(R.id.btn_checkout)
    Button btnCheckout;
    @BindView(R.id.img_action)
    ImageView imgOrder;
    private OrderAdapter adapter;
    private DaoRepository repository;
    private List<OrderItem> itemList;
    private BehaviorSubject<Double> dSubject;
    private double totalPrice;
    private int rowHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repository = new DaoRepository(this);
        itemList = new ArrayList<>();
        dSubject = BehaviorSubject.create();

        setRecyclerView();
        setImgAction();
        getOrder();
        observerSheetView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setRecyclerView() {
        adapter = new OrderAdapter(itemList, this);
        adapter.setAnInterface(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setImgAction() {
        imgOrder.setImageResource(R.drawable.ic_clear);
        imgOrder.setOnClickListener(view -> {
            this.finish();
            clearOrder(itemList);
        });
        imgOrder.setVisibility(View.VISIBLE);
    }

    private void getOrder() {
        // Get list item in Room db
        repository.getInOrder().observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<OrderItem> orderItems) -> {
                    if (orderItems.size() != 0) {
                        for (OrderItem item : orderItems) {
                            totalPrice += item.getTotalPrice();
                        }
                        dSubject.onNext(totalPrice);
                        itemList.addAll(orderItems);
                        adapter.notifyDataSetChanged();
                    }
                }, throwable -> Log.d(TAG, "getOrder: " + throwable));
    }

    private void updateOrder(OrderItem item, int position) {
        // Update item in order
        repository.updateOrder(item).subscribe(() -> {
            itemList.get(position).setQuantity(item.getQuantity());
            itemList.get(position).setTotalPrice(item.getTotalPrice());
            adapter.notifyItemChanged(position);
        }, throwable -> Log.d(TAG, "updateOrder: " + throwable));
    }

    private void removeInOrder(OrderItem item, int position) {
        // Remove item in order
        repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            itemList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, itemList.size());

            // update total price
            double newPrice = totalPrice - item.getTotalPrice();
            dSubject.onNext(newPrice);

            // update params
            updateRecyclerParam();
        }, throwable -> Log.d(TAG, "removeOrder: " + throwable));
        if (itemList.size() == 0) {
            this.finish();
        }
    }

    private void clearOrder(List<OrderItem> orderItems) {
        if (orderItems.size() != 0) {
            for (OrderItem item : orderItems) {
                // Set instance one by one
                // Then update into Room db
                item.setQuantity(0);
                item.setTotalPrice(0);
                repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, throwable -> Log.d(TAG, "clearOrder: " + throwable));
            }
            orderItems.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void showBottomSheet(int position) {
        // Transaction item and position to bottomSheet
        OrderItem item = itemList.get(position);
        Bundle b = new Bundle();
        b.putParcelable(KEY_DATA, item);
        b.putInt(KEY_POS, position);
        SheetOrderFragment f = new SheetOrderFragment();
        f.setAnInterface(this);
        f.setArguments(b);
        f.show(getSupportFragmentManager(), f.getTag());
    }

    private void observerSheetView() {
        dSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(aDouble -> {
            txtPrice.setText(String.format("%s $", aDouble));
        });
    }

    private void updateRecyclerParam() {
        // Update height RecyclerView dynamically when remove item
        this.runOnUiThread(() -> {
            int recyclerH = recyclerView.getHeight();
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, recyclerH - rowHeight));
        });
    }

    @Override
    public void onClick(int pos, int h) {
        // Callback to Adapter
        showBottomSheet(pos);
        // set height
        rowHeight = h;
    }

    @Override
    public void onEdit(OrderItem item, int position) {
        // Callback to bottomSheet
        if (item.getQuantity() != 0) {
            updateOrder(item, position);
        } else {
            removeInOrder(item, position);
        }
    }

    @OnClick(R.id.btn_checkout)
    public void onViewClicked() {
        Intent i;
        boolean isloggedin =  new SessionManager(this).isLoggedIn();
        if (isloggedin){
            i = new Intent(OrderActivity.this,CheckoutActivity.class);
        }else {
            i = new Intent(OrderActivity.this,SigninActivity.class);
        }
        startActivity(i);
    }
}
