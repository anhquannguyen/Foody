package com.example.anhqu.orderApp.ui.login;

import android.content.Context;

import com.example.anhqu.orderApp.data.network.ApiClient;
import com.example.anhqu.orderApp.data.network.ApiInterface;
import com.example.anhqu.orderApp.data.prefs.SessionManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {
    private Disposable disposable;
    private LoginView loginView;
    private SessionManager sessionManager;

    LoginPresenterImpl(LoginView loginView, Context context) {
        this.loginView = loginView;
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public void onLogin(String username, String password) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        disposable = api.login(username, password).subscribeOn(Schedulers.io())
                .map(users -> users.get(0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((user, throwable) -> {
                    if (user != null) {
                        loginView.onLoginSuccess();
                        sessionManager.create(user.getuId()
                                ,user.getuName()
                                ,user.getuPw()
                                ,user.getuFullName()
                                ,user.getuMobile());
                    } else {
                        loginView.onLoginFail();
                    }
                    if (throwable != null) {
                        loginView.onLoginError(throwable.toString());
                    }
                });
    }

    @Override
    public void onDispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
