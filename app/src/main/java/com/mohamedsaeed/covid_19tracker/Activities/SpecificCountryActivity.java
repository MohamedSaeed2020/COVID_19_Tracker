package com.mohamedsaeed.covid_19tracker.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;
import com.mohamedsaeed.covid_19tracker.R;
import com.mohamedsaeed.covid_19tracker.databinding.ActivitySpecifcCountryBinding;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SpecificCountryActivity extends AppCompatActivity {

    ActivitySpecifcCountryBinding binding;
    APIService apiService;
    String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySpecifcCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data from home fragment
        Intent intent = getIntent();
        searchKey = intent.getStringExtra("search");
        String TotalCases = intent.getStringExtra("totalCases");
        String TotalDeaths = intent.getStringExtra("totalDeaths");
        String TodayCases = intent.getStringExtra("todayCases");
        String TodayDeaths = intent.getStringExtra("todayDeaths");
        String TotalRecovered = intent.getStringExtra("totalRecovered");
        String StillActive = intent.getStringExtra("stillActive");



        //Actionbar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(searchKey + " Statistics");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);


         binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorAccent);
        setData(TotalCases, TotalDeaths, TodayCases, TodayDeaths, TotalRecovered, StillActive);
         binding.swipeRefreshLayout.setOnRefreshListener(() -> loadJSON(searchKey));

    }

    private void setData(String TotalCases, String TotalDeaths, String TodayCases, String TodayDeaths, String TotalRecovered, String StillActive) {
        binding.totalCasesNumber.setText(TotalCases);
        binding.totalDeathsNumber.setText(TotalDeaths);
        binding.todayCasesNumber.setText(TodayCases);
        binding.todayDeathsNumber.setText(TodayDeaths);
        binding.totalRecoveredNumber.setText(TotalRecovered);
        binding.stillActiveNumber.setText(StillActive);
    }

    private void loadJSON(String searchKey) {

        binding.swipeRefreshLayout.setRefreshing(true);  //To make the swiping circle loading
        //create api service
        apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        Single<CountryModel> observable=apiService.getSpecificCountryInfo(searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<CountryModel> observer=new SingleObserver<CountryModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(@NonNull CountryModel countryModel) {
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.totalCasesNumber.setText("" + countryModel.getCases());
                binding.totalDeathsNumber.setText("" + countryModel.getDeaths());
                binding.todayCasesNumber.setText("" + countryModel.getTodayCases());
                binding.todayDeathsNumber.setText("" + countryModel.getTodayDeaths());
                binding.totalRecoveredNumber.setText("" + countryModel.getRecovered());
                binding.stillActiveNumber.setText("" + countryModel.getActive());

            }

            @Override
            public void onError(@NonNull Throwable e) {
                binding.swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();            }
        };
        observable.subscribe(observer);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  //Go to previous activity
        return super.onSupportNavigateUp();
    }


}

