package com.example.anhqu.orderApp.ui.login;

public interface LoginView {
    void onLoginSuccess();

    void onLoginFail();

    void onLoginError(String message);
}
