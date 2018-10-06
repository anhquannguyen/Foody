package com.example.anhqu.foody.view;

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
import com.example.anhqu.foody.model.Food;
import com.example.anhqu.foody.model.OrderItem;
import com.example.anhqu.foody.model.api.ApiClient;
import com.example.anhqu.foody.model.api.ApiInterface;
import com.example.anhqu.foody.model.database.DaoRepository;
import com.example.anhqu.foody.view.adapter.FoodAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhqu on 6/22/2017.
 */

public class FoodActivity extends BaseActivity implements FoodAdapter.onClickInterface {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_action)
    ImageView imgOrder;
    private FoodAdapter adapter;
    private DaoRepository repository;
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
        repository = new DaoRepository(this);
        itemList = new ArrayList<>();

        setRecyclerView();
        getById(id);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_food;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isClear) {
            getById(id);
        }
        getCountInOrder();
    }

    @Override
    protected void onStop() {
        super.onStop();
        itemList.clear();
        adapter.notifyDataSetChanged();
        isClear = true;
        if (isAppear) {
            isAppear = false;
        }
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

    private void getCountInOrder() {
        repository.countInOrder().observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != 0) {
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
                    } else {
                        if (imgOrder.getVisibility() == View.VISIBLE) {
                            imgOrder.setVisibility(View.INVISIBLE);
                        }
                    }
                }, throwable -> Log.d(TAG, "getCountInOrder: " + throwable));
    }

    private void getApi(final String id) {
        progressBar.setVisibility(View.VISIBLE);
        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        api.get(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(foods -> {
                    Log.d(TAG, "getApi: " + foods.size());
                })
                .subscribe(foods -> {
                    if (foods != null) {
                        for (Food o : foods) {
                            itemList.add(new OrderItem(o));
                        }
                        addOrder(itemList, id);
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, throwable -> Log.d(TAG, "getApi: " + throwable));
    }

    private void getById(String id) {
        repository.getById(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderItems -> {
                    if (orderItems.size() != 0) {
                        if (itemList.size() != 0) {
                            itemList.clear();
                        }
                        itemList.addAll(orderItems);
                        adapter.notifyDataSetChanged();
                    } else {
                        getApi(id);
                    }
                }, throwable -> Log.d(TAG, "getById: " + throwable));
    }

    private void addOrder(List<OrderItem> item, String id) {
        repository.addOrder(item)
                .subscribe(() -> getById(id)
                        , throwable -> Log.d(TAG, "addOrder: " + throwable));
    }

    private void updateOrder(OrderItem item) {
        repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getCountInOrder
                        , throwable -> Log.d(TAG, "updateOrder: " + throwable));
    }

    private void removeOrder(OrderItem item) {
        showSnackbar(item);
        item.setQuantity(0);
        item.setTotalPrice(0);
        repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getCountInOrder
                        , throwable -> Log.d(TAG, "updateOrder: " + throwable));
    }

    private void showSnackbar(OrderItem item) {
        String message = item.getQuantity() + " items removed.";

        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void onAppear(View view) {
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.startAnimation(scale);
    }

    @Override
    public void onClick(OrderItem item) {
        updateOrder(item);
    }

    @Override
    public void onLongClick(OrderItem item) {
        removeOrder(item);
    }
}
