package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.ui.food.FoodActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CompleteActivity extends BaseActivity {
    @BindView(R.id.btn_done)
    Button btnDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_complete;
    }

    @OnClick(R.id.btn_done)
    public void onViewClicked() {
        onBackPressed();
    }
}
