package com.example.anhqu.foody.ui.checkout;

import android.content.Context;

import com.example.anhqu.foody.data.database.DaoRepository;
import com.example.anhqu.foody.data.database.model.Order;
import com.example.anhqu.foody.data.database.model.OrderItem;
import com.example.anhqu.foody.data.network.ApiClient;
import com.example.anhqu.foody.data.network.ApiInterface;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckoutPresenterImpl implements CheckoutPresenter {
    private CheckoutView checkoutView;
    private CompositeDisposable compositeDisposable;
    private DaoRepository repository;

    public CheckoutPresenterImpl(CheckoutView checkoutView, Context context) {
        this.checkoutView = checkoutView;
        this.repository = new DaoRepository(context);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadPayment() {
        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Disposable disposable = api.getPayment().subscribeOn(Schedulers.io())
                .subscribe(payments -> checkoutView.onLoadPayment(payments), throwable -> checkoutView.onError(throwable.toString()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void placeOrder(int uId, String setTime, String location, int payment) {
        Disposable disposable = repository.getInOrder()
                .subscribeOn(Schedulers.io())
                .map(orderItems -> {
                    Order order = new Order(uId, setTime, location, payment, orderItems);
                    checkOrder(order, orderItems);
                    return "";
                }).subscribe();
        compositeDisposable.add(disposable);
    }

    private void checkOrder(Order order, List<OrderItem> orderItems) {
        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        api.place(order).subscribeOn(Schedulers.io())
                .map(aBoolean -> {
                    if (aBoolean != null) {
                        if (aBoolean) {
                            checkoutView.onPlaceSucess();
                            clearOrders(orderItems);
                        } else checkoutView.onPlaceFailed();
                    }
                    return "";
                }).subscribe();
    }

    public void clearOrders(List<OrderItem> orderItems) {
        if (orderItems.size() != 0)
            for (OrderItem item : orderItems) {
                // Set instance one by one
                // Then update into Room db
                item.setQuantity(0);
                item.setTotalPrice(0);
                repository.updateOrder(item).subscribe();
            }
    }
}
