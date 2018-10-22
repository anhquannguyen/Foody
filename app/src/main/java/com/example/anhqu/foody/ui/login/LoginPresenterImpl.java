package com.example.anhqu.foody.ui.login;

import com.example.anhqu.foody.data.network.ApiClient;
import com.example.anhqu.foody.data.network.ApiInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {
    private Disposable disposable;
    private LoginView loginView;

    LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onLogin(String username, String password) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        disposable = api.login(username, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list, throwable) -> {
                    if (list != null) {
                        loginView.onLoginSuccess();
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
