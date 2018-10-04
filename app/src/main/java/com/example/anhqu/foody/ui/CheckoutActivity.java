package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.SessionManager;
import com.example.anhqu.foody.model.Payment;
import com.example.anhqu.foody.ui.adapter.PaymentAdapter;
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

public class CheckoutActivity extends BaseActivity {
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
    List<Payment> payments;
    PlacePicker.IntentBuilder builder;
    @BindView(R.id.img_action)
    ImageView imgAction;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sessionManager = new SessionManager(this);
        builder = new PlacePicker.IntentBuilder();
        setRecyclerView();
        setTextView();
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
        payments = new Payment().getPayment();
        Log.d(TAG, "setRecyclerView: " + payments.get(0).getpName());
        adapter = new PaymentAdapter(payments, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
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
                Toast.makeText(this, "Update's coming...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
