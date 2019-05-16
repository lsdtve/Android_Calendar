package com.example.android_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_TIME_OUT = 1300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(()->{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
        }, SPLASH_TIME_OUT);
    }
}