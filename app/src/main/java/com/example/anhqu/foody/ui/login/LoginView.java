package com.example.anhqu.foody.ui.login;

public interface LoginView {
    void onLoginSuccess();

    void onLoginFail();

    void onLoginError(String message);
}
