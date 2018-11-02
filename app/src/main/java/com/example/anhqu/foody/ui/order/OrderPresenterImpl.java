package com.example.anhqu.foody.ui.order;

import android.content.Context;

import com.example.anhqu.foody.data.database.DaoRepository;
import com.example.anhqu.foody.data.database.model.OrderItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class OrderPresenterImpl implements OrderPresenter {
    private OrderView orderView;
    private CompositeDisposable compositeDisposable;
    private DaoRepository repository;
    private BehaviorSubject<Double> subject;

    public OrderPresenterImpl(OrderView orderView, Context context) {
        this.orderView = orderView;
        this.compositeDisposable = new CompositeDisposable();
        this.repository = new DaoRepository(context);
        subject = BehaviorSubject.create();
    }

    public void setTotalPrice(Double d) {
        subject.onNext(d);
    }

    @Override
    public void getOrders() {
        Disposable disposable = repository.getInOrder().observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<OrderItem> orderItems) -> {
                    orderView.onloadData(orderItems);

                }, throwable -> orderView.onHandleDataFailed(throwable.toString()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void updateOrder(OrderItem item, int position) {
        Disposable disposable = repository.updateOrder(item)
                .subscribe(() -> orderView.onUpdateSuccess(position), throwable -> orderView.onHandleDataFailed(throwable.toString()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteOrder(OrderItem item, int position) {
        Disposable disposable = repository.updateOrder(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> orderView.onDeleteSuccess(item, position));
        compositeDisposable.add(disposable);
    }

    @Override
    public void clearOrders(List<OrderItem> orderItems) {
        if (orderItems.size() != 0)
            for (OrderItem item : orderItems) {
                // Set instance one by one
                // Then update into Room db
                item.setQuantity(0);
                item.setTotalPrice(0);
                repository.updateOrder(item).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        orderView.onClearSuccess();
    }

    @Override
    public void updateTotalPrice() {
        Disposable disposable = subject.observeOn(AndroidSchedulers.mainThread()).subscribe(aDouble -> orderView.totalPriceUpdate(aDouble));
        compositeDisposable.add(disposable);
    }

    void clearDisposables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
