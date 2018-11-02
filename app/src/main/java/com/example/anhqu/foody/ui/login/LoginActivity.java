package com.example.anhqu.foody.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.ui.BaseActivity;
import com.example.anhqu.foody.ui.checkout.CheckOutActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etx_username)
    EditText etxUsername;
    @BindView(R.id.etx_pw)
    EditText etxPw;
    private LoginPresenterImpl presenter;

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

        presenter = new LoginPresenterImpl(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDispose();
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

    private void login() {
        String username = etxUsername.getText().toString().trim();
        String password = etxPw.getText().toString().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            presenter.onLogin(username, password);
        } else {
            Toast.makeText(this, "Please enter the credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.txt_forget, R.id.btn_signin, R.id.txt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forget:
                break;
            case R.id.btn_signin:
                login();
                break;
            case R.id.txt_register:
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFail() {
        Toast.makeText(this, "Username or password does not matches!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError(String message) {
        Log.d(TAG, "onLoginError: " + message);
    }
}
