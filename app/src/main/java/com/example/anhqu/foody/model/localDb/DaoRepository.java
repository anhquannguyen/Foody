package com.example.anhqu.foody.model.localDb;

import android.content.Context;

import com.example.anhqu.foody.model.OrderItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhqu on 9/5/2018.
 */

public class DaoRepository {
    private Context context;

    public DaoRepository(Context context) {
        this.context = context;
    }

    public Maybe<List<OrderItem>> getInOrder() {
        return Database.getInstance(context).Daodb().getInOrder().subscribeOn(Schedulers.io());
    }

    public Maybe<List<OrderItem>> getById(String id) {
        return Database.getInstance(context).Daodb().getById(id).subscribeOn(Schedulers.io());
    }

    public Single<Integer> countInOrder() {
        return Database.getInstance(context).Daodb().getCountInOrder().subscribeOn(Schedulers.io());
    }

    public Completable addOrder(List<OrderItem> item) {
        return Completable.fromAction(() -> Database.getInstance(context).Daodb().insert(item)).subscribeOn(Schedulers.io());
    }

    public Completable updateOrder(OrderItem item) {
        return Completable.fromAction(() -> Database.getInstance(context)
                .Daodb().edit(item)).subscribeOn(Schedulers.io());

    }

/*    public Completable removeOrder(OrderItem item) {
        return Completable.fromAction(() -> Database.getInstance(context)
                .Daodb().remove(item)).subscribeOn(Schedulers.io());
    }

    public Completable clearAllOrder() {
        return Completable.fromAction(() -> Database.getInstance(context)
                .Daodb().clearOrder()).subscribeOn(Schedulers.io());
    }*/
}
