package com.example.yanglin.arcface.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;



import com.example.yanglin.arcface.R;
import com.example.yanglin.arcface.models.User;
import com.example.yanglin.arcface.utils.Cache;
import com.example.yanglin.arcface.utils.systemBar.SystemBarUI;


/**
 * Created by yanglin on 18-3-18.
 */

public class SplashActivity extends AppCompatActivity{
    protected Handler mHandler = new Handler();

    Cache cache = new Cache();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemBarUI.initSystemBar(this, R.color.actionTitle);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = cache.getUser(getFilesDir());
                if(user != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                SplashActivity.this.finish();
            }
        },3000);

    }
}
