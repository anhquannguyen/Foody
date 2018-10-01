package com.example.anhqu.foody;

import android.app.Application;

/**
 * Created by anhquan on 3/13/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
