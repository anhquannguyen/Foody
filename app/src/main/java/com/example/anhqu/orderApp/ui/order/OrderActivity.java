package com.example.anhqu.orderApp.ui.order;

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

import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.data.database.model.OrderItem;
import com.example.anhqu.orderApp.data.prefs.SessionManager;
import com.example.anhqu.orderApp.ui.BaseActivity;
import com.example.anhqu.orderApp.ui.checkout.CheckOutActivity;
import com.example.anhqu.orderApp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhqu on 7/5/2018.
 */

public class OrderActivity extends BaseActivity implements OrderAdapter.onClickInterface,
        DetailSheetFragment.EditOrderInterface, OrderView {
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
    private OrderPresenterImpl presenter;
    private List<OrderItem> itemList;
    private double totalPrice;
    private double excPrice;
    private int rowHeight;
    private static OrderActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        presenter = new OrderPresenterImpl(this, this);
        itemList = new ArrayList<>();
        instance = this;

        setRecyclerView();
        setImgAction();
        presenter.getOrders();
        presenter.updateTotalPrice();
    }

    public static OrderActivity getInstance(){
        return instance;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        imgOrder.setVisibility(View.VISIBLE);
    }

    private void showBottomSheet(int position) {

        // Transaction item and position to bottomSheet
        OrderItem item = itemList.get(position);
        Bundle b = new Bundle();
        b.putParcelable(KEY_DATA, item);
        b.putInt(KEY_POS, position);
        DetailSheetFragment f = new DetailSheetFragment();
        f.setAnInterface(this);
        f.setArguments(b);
        f.show(getSupportFragmentManager(), f.getTag());
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
        // Set height
        rowHeight = h;
    }

    @Override
    public void onEdit(OrderItem item, int position, double excTotalP) {
        excPrice = excTotalP;
        if (item.getQuantity() != 0)
            presenter.updateOrder(item, position);
        else presenter.deleteOrder(item, position);
    }

    @OnClick({R.id.btn_checkout, R.id.img_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                Intent i;
                boolean isloggedin = new SessionManager(this).isLoggedIn();
                if (isloggedin)
                    i = new Intent(OrderActivity.this, CheckOutActivity.class);
                else
                    i = new Intent(OrderActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.img_action:
                presenter.clearOrders(itemList);
                break;
        }
    }

    @Override
    public void onloadData(List<OrderItem> orderItems) {
        if (orderItems.size() != 0)
            for (OrderItem item : orderItems) {
                totalPrice += item.getTotalPrice();
            }
        itemList.addAll(orderItems);
        adapter.notifyDataSetChanged();
        presenter.setTotalPrice(totalPrice);
    }

    @Override
    public void onUpdateSuccess(int position) {
        adapter.notifyItemChanged(position);
        presenter.setTotalPrice(totalPrice += excPrice);
    }

    @Override
    public void onDeleteSuccess(OrderItem item, int position) {
        itemList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, itemList.size());
        if (itemList.size() == 0) {
            this.finish();
            presenter.clearDisposables();
        } else {
            presenter.setTotalPrice(totalPrice += excPrice);

            // Update params
            updateRecyclerParam();
        }
    }

    @Override
    public void onClearSuccess() {
        this.finish();
        presenter.clearDisposables();
    }

    @Override
    public void totalPriceUpdate(double total) {
        txtPrice.setText(String.format("%s $", total));
    }

    @Override
    public void onHandleDataFailed(String error) {
        Log.d(TAG, "onHandleDataFailed: " + error);
    }
}
