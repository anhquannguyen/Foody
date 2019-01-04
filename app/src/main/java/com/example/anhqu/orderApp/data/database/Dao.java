package com.example.anhqu.orderApp.data.database;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.anhqu.orderApp.data.database.model.OrderItem;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by anhqu on 2/26/2018.
 */
@androidx.room.Dao
public interface Dao {

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
