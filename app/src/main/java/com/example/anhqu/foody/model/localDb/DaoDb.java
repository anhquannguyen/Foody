package com.example.anhqu.foody.model.localDb;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.anhqu.foody.model.OrderItem;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by anhqu on 2/26/2018.
 */
@android.arch.persistence.room.Dao
public interface DaoDb {

    @Insert
    long[] insert(List<OrderItem> orderItem);

    @Update
    void edit(OrderItem orderItemItems);

    /*@Delete
    void remove(OrderItem orderItem)*/

    @Query("SELECT * FROM OrderItem WHERE quantity >= 1")
    Maybe<List<OrderItem>> getInOrder();

    @Query("SELECT * FROM OrderItem WHERE mId LIKE :id")
    Maybe<List<OrderItem>> getById(String id);

    @Query("SELECT COUNT(*) FROM OrderItem WHERE quantity >= 1")
    Single<Integer> getCountInOrder();

   /* @Query("DELETE FROM OrderItem WHERE quantity >= 1")
    void clearOrder();*/
}
