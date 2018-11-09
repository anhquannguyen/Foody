package com.example.anhqu.foody.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.ui.main.MainActivity;

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

        ViewCompat.animate(imgLogo)
                .translationY(-250)
                .setStartDelay(ANIM_DELAY)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new DecelerateInterpolator(1.2f))
                .start();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(SplashScActivity.this, MainActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }, ACTIVITY_DELAY);
    }
}
