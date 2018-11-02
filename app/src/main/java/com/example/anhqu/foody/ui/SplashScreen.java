package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.ui.main.MainActivity;

import butterknife.BindView;

public class SplashScreen extends BaseActivity {
    @BindView(R.id.img_logo)
    ImageView imgLogo;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.splash_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ViewCompat.animate(imgLogo)
                    .translationY(-250f)
                    .setStartDelay(100)
                    .setDuration(500).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }, 1000);
    }
}
