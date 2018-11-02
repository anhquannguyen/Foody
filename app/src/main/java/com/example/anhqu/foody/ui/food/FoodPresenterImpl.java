package com.example.anhqu.foody.ui.food;

import android.content.Context;

import com.example.anhqu.foody.data.database.DaoRepository;
import com.example.anhqu.foody.data.database.model.Food;
import com.example.anhqu.foody.data.database.model.OrderItem;
import com.example.anhqu.foody.data.network.ApiClient;
import com.example.anhqu.foody.data.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodPresenterImpl implements FoodPresenter {
    private FoodView foodView;
    private DaoRepository repository;
    private CompositeDisposable compositeDisposable;

    public FoodPresenterImpl(FoodView foodView, Context context) {
        this.foodView = foodView;
        this.repository = new DaoRepository(context);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getFoods(String id) {
        Disposable disposable = repository.getById(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderItems -> {
                    if (orderItems.size() != 0) {
                        foodView.onLoadSuccess(orderItems);
                    } else {
                        getApi(id);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void getCount() {
        Disposable disposable = repository.countInOrder().observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != 0) {
                        foodView.onOrderVisible();
                    } else {
                        foodView.onOrderInvisible();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void addOrder(OrderItem item) {
        Disposable disposable = repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getCount);
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteOrder(OrderItem item) {
        foodView.onViewRemoved(item);
        item.setQuantity(0);
        item.setTotalPrice(0);
        Disposable disposable = repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getCount();
                });
        compositeDisposable.add(disposable);
    }

    private void getApi(String id) {
        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Disposable disposable = api.getFood(id).subscribeOn(Schedulers.io()).subscribe(foods -> {
            if (foods != null) {
                List<OrderItem> itemList = new ArrayList<>();
                for (Food o : foods) {
                    itemList.add(new OrderItem(o));
                }
                addOrder(itemList, id);
            } else {
                foodView.onLoadFailed();
            }
        }, throwable -> foodView.onLoadError(throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void addOrder(List<OrderItem> item, String id) {
        Disposable disposable = repository.addOrder(item)
                .subscribe(() -> getFoods(id));
        compositeDisposable.add(disposable);
    }

    void clearDisposables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
