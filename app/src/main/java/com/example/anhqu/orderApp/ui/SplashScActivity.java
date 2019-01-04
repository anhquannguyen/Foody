package com.example.anhqu.orderApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.ui.main.MainActivity;

import butterknife.BindView;

public class SplashScActivity extends BaseActivity {
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    private static final long ANIM_DELAY = 300; // Delay when view's appear
    private static final long ANIM_DURATION = 1000; // Time to exec
    private static final long ACTIVITY_DELAY = 2000;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash_sc;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ContextCompat.getDrawable(this,R.drawable.image_main);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ViewCompat.animate(imgLogo)
                    .translationY(-250)
                    .setStartDelay(ANIM_DELAY)
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator(1.2f))
                    .start();

            Intent i = new Intent(SplashScActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }, ACTIVITY_DELAY);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
