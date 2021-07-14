package com.mohamedsaeed.covid_19tracker.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mohamedsaeed.covid_19tracker.R;
import com.mohamedsaeed.covid_19tracker.databinding.ActivityDashboardBinding;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    ActionBar actionBar;

    ActivityDashboardBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Very important .. loadLocale should be before setContentView
        loadLocale();
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Actionbar
        actionBar = getSupportActionBar();
        //Home fragment transaction, Default on start
        assert actionBar != null;
        actionBar.setTitle(getResources().getString(R.string.home)); //change action bar title
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragmentTransaction.replace(R.id.content, homeFragment, "HOME");
        homeFragmentTransaction.commit();

        //Bottom Navigation
        binding.navigation.setOnNavigationItemSelectedListener(selectedListener);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    //Handle items clicks
                    switch (item.getItemId()) {
                        case R.id.nav_home:

                            //Home fragment transaction
                            actionBar.setTitle(getResources().getString(R.string.home)); //change action bar title
                            HomeFragment homeFragment = new HomeFragment();
                            FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            homeFragmentTransaction.replace(R.id.content, homeFragment, "HOME");
                            homeFragmentTransaction.commit();
                            return true;

                        case R.id.nav_global:
                            //Countries fragment transaction
                            actionBar.setTitle(getResources().getString(R.string.global_statistics)); //change action bar title
                            CountriesFragment countriesFragment = new CountriesFragment();
                            FragmentTransaction profileFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            profileFragmentTransaction.replace(R.id.content, countriesFragment, "COUNTRIES");
                            profileFragmentTransaction.commit();
                            return true;

                        case R.id.nav_map:
                            //Map fragment transaction
                            actionBar.setTitle(getResources().getString(R.string.map)); //change action bar title
                            MapFragment mapFragment = new MapFragment();
                            FragmentTransaction usersFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            usersFragmentTransaction.replace(R.id.content, mapFragment, "MAP");
                            usersFragmentTransaction.commit();
                            return true;

                    }
                    return false;
                }
            };


    //initialize option menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        //inflating menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //handle option menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //get item id
        int id = item.getItemId();
        if (id == R.id.change_language) {
            //show alert dialog to display a list of languages, one can be selected
            //showChangeLanguageDialog();
            startActivity(new Intent(this, LanguageActivity.class));
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLocale(String languageNotation) {
        Locale locale = new Locale(languageNotation);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, metrics);

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", languageNotation);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadLocale() {

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String lang = Locale.getDefault().getDisplayLanguage();
        String defaultLan;
        if (lang.equals("English")) {
            defaultLan = "en";
        } else if (lang.equals("العربية")) {
            defaultLan = "ar";
        } else {
            defaultLan = "en";
        }
        String language = sharedPreferences.getString("My_Lang", defaultLan);
        setLocale(language);
    }


}
