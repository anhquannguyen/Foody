package com.example.anhqu.orderApp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.example.anhqu.orderApp.R;

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
