package com.mohamedsaeed.covid_19tracker.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;
import com.mohamedsaeed.covid_19tracker.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecifCountryActivity extends AppCompatActivity {

    APIService apiService;
    TextView totalCases, totalDeaths, todayCases, todayDeaths, totalRecovered, stillActive;
    private SwipeRefreshLayout swipeRefreshLayout;
    String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specifc_country);

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

        //init views
        totalCases = findViewById(R.id.total_cases_number);
        totalDeaths = findViewById(R.id.total_deaths_number);
        todayCases = findViewById(R.id.today_cases_number);
        todayDeaths = findViewById(R.id.today_deaths_number);
        totalRecovered = findViewById(R.id.total_recovered_number);
        stillActive = findViewById(R.id.still_active_number);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorAccent);
        setData(TotalCases, TotalDeaths, TodayCases, TodayDeaths, TotalRecovered, StillActive);
        swipeRefreshLayout.setOnRefreshListener(() -> loadJSON(searchKey));

    }

    private void setData(String TotalCases, String TotalDeaths, String TodayCases, String TodayDeaths, String TotalRecovered, String StillActive) {
        totalCases.setText(TotalCases);
        totalDeaths.setText(TotalDeaths);
        todayCases.setText(TodayCases);
        todayDeaths.setText(TodayDeaths);
        totalRecovered.setText(TotalRecovered);
        stillActive.setText(StillActive);
    }

    private void loadJSON(String searchKey) {
        swipeRefreshLayout.setRefreshing(true);  //To make the swiping circle loading
        //create api service
        apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        apiService.getSpecificCountryInfo(searchKey)
                .enqueue(new Callback<CountryModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {

                        if (response.code() == 200) {
                            swipeRefreshLayout.setRefreshing(false);
                            assert response.body() != null;
                            totalCases.setText("" + response.body().getCases());
                            totalDeaths.setText("" + response.body().getDeaths());
                            todayCases.setText("" + response.body().getTodayCases());
                            todayDeaths.setText("" + response.body().getTodayDeaths());
                            totalRecovered.setText("" + response.body().getRecovered());
                            stillActive.setText("" + response.body().getActive());

                        }
                    }

                    @Override
                    public void onFailure(Call<CountryModel> call, Throwable t) {

                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  //Go to previous activity
        return super.onSupportNavigateUp();
    }


}

