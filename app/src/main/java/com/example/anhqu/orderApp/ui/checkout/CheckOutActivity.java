package com.example.anhqu.orderApp.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.data.database.model.Payment;
import com.example.anhqu.orderApp.data.prefs.SessionManager;
import com.example.anhqu.orderApp.ui.BaseActivity;
import com.example.anhqu.orderApp.ui.CompleteActivity;
import com.example.anhqu.orderApp.ui.order.OrderActivity;
import com.example.anhqu.orderApp.utils.RecyclerTouchListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhqu on 9/23/2018.
 */

public class CheckOutActivity extends BaseActivity implements CheckoutView {
    private static final int PLACE_PICKER_REQUEST = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.act_add_loct)
    TextView actAddLoct;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    PaymentAdapter adapter;
    List<Payment> paymentList;
    PlacePicker.IntentBuilder builder;
    @BindView(R.id.img_action)
    ImageView imgAction;
    SessionManager sessionManager;
    CheckoutPresenterImpl checkoutPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkoutPresenter = new CheckoutPresenterImpl(this, this);
        sessionManager = new SessionManager(this);
        builder = new PlacePicker.IntentBuilder();
        setRecyclerView();
        setTextView();
        checkoutPresenter.loadPayment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_checkout;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setRecyclerView() {
        paymentList = new Payment().getPayment();
        adapter = new PaymentAdapter(paymentList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setTextView() {
        String address = sessionManager.getAddress();
        String mobile = sessionManager.getUser().getuMobile();
        txtLocation.setText(address);
        txtPhone.setText(mobile);
    }

    private void initPlacePicker() {
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void setPlaceOrder() {
        int uId = sessionManager.getUserId();
        String location = sessionManager.getAddress();
        checkoutPresenter.placeOrder(uId, "2018-11-10 20:33:10", location, 3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                sessionManager.putPlace(place);
                setTextView();
            }
        }
    }

    @OnClick({R.id.card_datetime, R.id.act_add_loct, R.id.btn_place_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_datetime:
                break;
            case R.id.act_add_loct:
                initPlacePicker();
                break;
            case R.id.btn_place_order:
                setPlaceOrder();
                break;
        }
    }

    @Override
    public void onPlaceSucess() {
        Intent i = new Intent(CheckOutActivity.this, CompleteActivity.class);
        startActivity(i);
        finish();
        if (OrderActivity.getInstance() != null)
            OrderActivity.getInstance().finish();
    }

    @Override
    public void onPlaceFailed() {

    }

    @Override
    public void onLoadPayment(List<Payment> payments) {
        paymentList.addAll(payments);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        Log.d(TAG, "onError: " + msg);
    }
}
