package com.example.anhqu.foody.model.api;

import com.example.anhqu.foody.model.Food;
import com.example.anhqu.foody.model.Menu;
import com.example.anhqu.foody.model.Order;
import com.example.anhqu.foody.model.User;

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
 * Created by anhquan on 3/20/2017.
 */

public interface ApiInterface {

    @GET("menu")
    Flowable<List<Menu>> get();

    @GET("menu/{id}/food")
    Observable<List<Food>> get(@Path("id") String Id);

    @POST("user/login")
    @FormUrlEncoded
    Single<List<User>> login(@Field("user_name") String username, @Field("user_pw") String user_pw);

    @POST("order/place")
    Single<Boolean> place(@Body Order order);

  /*  @Field("user_id") int userId,
    @Field("set_time") String time,
    @Field("user_location") String location,
    @Field("user_payment") int payment,
    @Field("list_item") List<OrderItem> items*/
}
