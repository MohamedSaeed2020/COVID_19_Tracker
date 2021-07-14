package com.mohamedsaeed.covid_19tracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedsaeed.covid_19tracker.R;
import com.mohamedsaeed.covid_19tracker.databinding.ActivityLanguageBinding;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    ActivityLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Actionbar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getResources().getString(R.string.language));

        binding.englishBtn.setOnClickListener(view -> {
            setLocale("en");
            recreate();
        });

        binding.arabicBtn.setOnClickListener(view -> {
            setLocale("ar");
            recreate();
        });
    }

    private void setLocale(String languageNotation) {
        Locale locale = new Locale(languageNotation);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, metrics);

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", languageNotation);
        editor.apply();
    }

    //should be written to go to dash board activity to load local not to home fragment
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
