package com.example.anhqu.foody.data.network;

import com.example.anhqu.foody.data.database.model.Food;
import com.example.anhqu.foody.data.database.model.Menu;
import com.example.anhqu.foody.data.database.model.Order;
import com.example.anhqu.foody.data.database.model.Payment;
import com.example.anhqu.foody.data.database.model.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by anhquan on 3/20/2018.
 */

public interface ApiInterface {

    @GET("menu")
    Flowable<List<Menu>> getMenu();

    @GET("menu/{id}/food")
    Observable<List<Food>> getFood(@Path("id") String Id);

    @POST("user/login")
    @FormUrlEncoded
    Single<List<User>> login(@Field("user_name") String username, @Field("user_pw") String user_pw);

    @GET("payment")
    Observable<List<Payment>> getPayment();

    /*@GET("offer")
    Observable<List<Offer>> getOffer();*/

    @POST("order/place")
    Single<Boolean> place(@Body Order order);

    @GET("user/{id}/orders")
    Flowable<List<Order>> getOrder(@Path("id") String id);
}
