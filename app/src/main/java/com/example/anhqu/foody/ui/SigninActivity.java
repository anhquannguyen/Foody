package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.SessionManager;
import com.example.anhqu.foody.model.api.ApiClient;
import com.example.anhqu.foody.model.api.ApiInterface;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SigninActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etx_username)
    EditText etxUsername;
    @BindView(R.id.etx_pw)
    EditText etxPw;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void checkLogin() {
        String username = etxUsername.getText().toString().trim();
        String password = etxPw.getText().toString().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            login(username, password);
        } else {
            Toast.makeText(this, "Please enter the credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    private void login(String username, String user_pw) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        api.login(username, user_pw).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe((list, throwable) -> {
            if (list != null) {
                new SessionManager(this).create(list.get(0).getuId(),
                        list.get(0).getuName(), list.get(0).getuPw(), list.get(0).getuFullName(), list.get(0).getuMobile());
                Intent i = new Intent(SigninActivity.this,CheckoutActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Username or password does not match!", Toast.LENGTH_SHORT).show();
            }
            if (throwable != null) {
                Log.d(TAG, "login: " + throwable);
            }
        });
    }

    @OnClick({R.id.txt_forget, R.id.btn_signin, R.id.txt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forget:
                break;
            case R.id.btn_signin:
                checkLogin();
                break;
            case R.id.txt_register:
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
