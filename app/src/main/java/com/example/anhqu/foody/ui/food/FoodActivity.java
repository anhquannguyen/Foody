package com.example.anhqu.foody.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.data.database.model.OrderItem;
import com.example.anhqu.foody.ui.BaseActivity;
import com.example.anhqu.foody.ui.order.OrderActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by anhqu on 6/22/2017.
 */

public class FoodActivity extends BaseActivity implements FoodAdapter.onClickInterface, FoodView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_action)
    ImageView imgOrder;
    private FoodPresenterImpl foodPresenter;
    private FoodAdapter adapter;
    private List<OrderItem> itemList;
    private String id;
    private boolean isClear = false;
    private boolean isAppear = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        if (i != null) {
            id = i.getStringExtra("menu_id");
        }
        foodPresenter = new FoodPresenterImpl(this, this);
        itemList = new ArrayList<>();

        setRecyclerView();
        foodPresenter.getFoods(id);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_food;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isClear) {
            foodPresenter.getFoods(id);
        }
        foodPresenter.getCount();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearAdapter();
        if (isAppear) {
            isAppear = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodPresenter.clearDisposables();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setRecyclerView() {
        adapter = new FoodAdapter(itemList, this);
        adapter.setAnInterface(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void clearAdapter(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        isClear = true;
    }

    public void onAppear(View view) {
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.startAnimation(scale);
    }

    @Override
    public void onClick(OrderItem item) {
        foodPresenter.addOrder(item);
    }

    @Override
    public void onLongClick(OrderItem item) {
        foodPresenter.deleteOrder(item);
    }

    @Override
    public void onLoadSuccess(List<OrderItem> orderItems) {
        if (itemList.size() != 0) {
            itemList.clear();
        }
        itemList.addAll(orderItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String message) {
        Log.d(TAG, "onLoadFailed: " + message);
    }

    @Override
    public void onLoadError(String error) {
        Log.d(TAG, "onLoadError: " + error);
    }

    @Override
    public void onOrderVisible() {
        imgOrder.setImageResource(R.drawable.ic_cart);
        imgOrder.setOnClickListener(view -> {
            Intent i = new Intent(this, OrderActivity.class);
            startActivity(i);
        });
        imgOrder.setVisibility(View.VISIBLE);
        if (!isAppear) {
            isAppear = true;
            onAppear(imgOrder);
        }
    }

    @Override
    public void onOrderInvisible() {
        if (imgOrder.getVisibility() == View.VISIBLE) {
            imgOrder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onViewRemoved(OrderItem item) {
        String message = item.getQuantity() + " items removed.";
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
