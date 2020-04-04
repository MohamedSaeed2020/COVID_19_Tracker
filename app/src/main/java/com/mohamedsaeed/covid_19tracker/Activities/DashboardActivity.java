package com.mohamedsaeed.covid_19tracker.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mohamedsaeed.covid_19tracker.R;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Very important .. loadLocale should be before setContentView
        loadLocale();
        setContentView(R.layout.activity_dashboard);

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
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
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

    private void showChangeLanguageDialog() {


        /*Show dialog containing option
         * 1) English
         * 2) Arabic*/

        //Options to be shown in the dialog
        String[] options = {getResources().getString(R.string.en_language), getResources().getString(R.string.ar_language)};

        //First method (Custom Dialog)
               /* new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getResources().getString(R.string.choose_language))
                .setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //English
                            setLocale("en");
                            recreate();

                        } else if (i == 1) {
                            //Arabic
                            setLocale("ar");
                            recreate();
                        }

                        //dismiss alert dialog when language is selected
                        dialogInterface.dismiss();
                    }
                })
                .create()
        .show();*/

        //Second method
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);

        TextView textView = new TextView(this);
        textView.setText(getResources().getString(R.string.choose_language));
        Typeface typeface = ResourcesCompat.getFont(this, R.font.cairo);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(20);
        textView.setTypeface(typeface);

        //Set title
        builder.setCustomTitle(textView);
        // builder.setTitle(getString(R.string.choose_language));
        // checkedItem specifies which item is checked. If -1 no items are checked.
        builder.setSingleChoiceItems(options, -1, (dialogInterface, i) -> {
            if (i == 0) {
                //English
                setLocale("en");
                recreate();

                /*Cause this Activity to be recreated with a new instance.
                This results in essentially the same flow as when the Activity is created due to a configuration change.
                the current instance will go through its lifecycle to onDestroy() and a new instance then created after it.*/

            } else if (i == 1) {
                //Arabic
                setLocale("ar");
                recreate();
            }

            //dismiss alert dialog when language is selected
            dialogInterface.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        //show dialog
        alertDialog.show();
    }


    private void setLocale(String languageNotation) {
        Locale locale = new Locale(languageNotation);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        configuration.setLocale(locale);


         /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            configuration.setLocale(locale);
          }else {
            configuration.locale = locale;
         }*/

        getResources().updateConfiguration(configuration, metrics);

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", languageNotation);
        editor.apply();
    }

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
