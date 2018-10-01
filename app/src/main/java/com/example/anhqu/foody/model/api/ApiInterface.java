package com.example.anhqu.foody.model.api;

import com.example.anhqu.foody.model.Food;
import com.example.anhqu.foody.model.Menu;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by anhquan on 3/20/2017.
 */

public interface ApiInterface {

    @GET("menu")
    Flowable<List<Menu>> getMenuList();

    @GET("menu/{id}/food")
    Observable<List<Food>> getFoodsByMenu(@Path("id") String Id);

}
