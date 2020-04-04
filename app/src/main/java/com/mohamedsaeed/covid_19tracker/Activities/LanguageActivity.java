package com.mohamedsaeed.covid_19tracker.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.mohamedsaeed.covid_19tracker.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    Button englishBtn, arabicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        englishBtn = findViewById(R.id.englishBtn);
        arabicBtn = findViewById(R.id.arabicBtn);

        //Actionbar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getResources().getString(R.string.language));

        englishBtn.setOnClickListener(view -> {
            setLocale("en");
            recreate();
        });

        arabicBtn.setOnClickListener(view -> {
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
