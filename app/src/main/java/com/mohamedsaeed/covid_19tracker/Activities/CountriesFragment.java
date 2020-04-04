package com.mohamedsaeed.covid_19tracker.Activities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamedsaeed.covid_19tracker.Models.GlobalInfoModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;
import com.mohamedsaeed.covid_19tracker.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CountriesFragment extends Fragment {

    private TextView totalCases, totalDeaths, totalRecovered;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countries, container, false);

        //init views
        totalCases = view.findViewById(R.id.total_cases_number);
        totalDeaths = view.findViewById(R.id.total_deaths_number);
        totalRecovered = view.findViewById(R.id.total_recovered_number);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorAccent);

        onLoadingSwipeRefresh();

        //swipeRefreshLayout setOnRefreshListener
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        return view;
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);  //To make the swiping circle loading
        //create api service
        APIService apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        apiService.getGlobalInfo()
                .enqueue(new Callback<GlobalInfoModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<GlobalInfoModel> call, Response<GlobalInfoModel> response) {
                        if (response.code() == 200) {
                            swipeRefreshLayout.setRefreshing(false);
                            assert response.body() != null;
                            totalCases.setText("" + response.body().getCases());
                            totalDeaths.setText("" + response.body().getDeaths());
                            totalRecovered.setText("" + response.body().getRecovered());

                        }
                    }

                    @Override
                    public void onFailure(Call<GlobalInfoModel> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //Run when you open the app and when you search for some thing and click search icon bun on single thread
    private void onLoadingSwipeRefresh() {
        swipeRefreshLayout.post(this::loadData);
    }


}
