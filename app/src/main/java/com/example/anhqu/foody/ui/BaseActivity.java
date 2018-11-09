package com.example.anhqu.foody.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.anhqu.foody.services.ConnectivityReceiver;
import com.example.anhqu.foody.services.MyApplication;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Created by anhqu on 7/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    protected String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
    }

    protected abstract @LayoutRes
    int getLayoutResourceId();

    public static Observable<Boolean> getNetworkStatus() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return Observable.just(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showStatus(isConnected);
    }

    // Showing the status in Snackbar
    private void showStatus(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Connection established.";
        } else {
            message = "No connection.";
        }
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
