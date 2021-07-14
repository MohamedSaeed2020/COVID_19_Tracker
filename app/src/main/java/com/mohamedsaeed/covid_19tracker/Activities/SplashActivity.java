package com.mohamedsaeed.covid_19tracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.style.Wave;
import com.mohamedsaeed.covid_19tracker.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //progress bar
        Wave wave = new Wave();
        binding.spinKit.setIndeterminateDrawable(wave);

        //start DashboardActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

}
