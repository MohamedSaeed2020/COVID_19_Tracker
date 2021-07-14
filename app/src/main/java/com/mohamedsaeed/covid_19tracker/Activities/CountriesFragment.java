package com.mohamedsaeed.covid_19tracker.Activities;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mohamedsaeed.covid_19tracker.R;
import com.mohamedsaeed.covid_19tracker.ViewModels.GlobalInfoViewModel;
import com.mohamedsaeed.covid_19tracker.databinding.FragmentCountriesBinding;


public class CountriesFragment extends Fragment {
    FragmentCountriesBinding binding;
    GlobalInfoViewModel globalInfoViewModel;

    public CountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCountriesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //init globalInfoViewModel
        globalInfoViewModel = new ViewModelProvider(this).get(GlobalInfoViewModel.class);
        globalInfoViewModel.init();

        //swipeRefreshLayout
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorAccent);
        onLoadingSwipeRefresh();

        //swipeRefreshLayout setOnRefreshListener
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadData);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadData() {
        binding.swipeRefreshLayout.setRefreshing(true);  //To make the swiping circle loading
        globalInfoViewModel.getStatistics().observe(this, globalInfoModel -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.totalCasesNumber.setText("" + globalInfoModel.getCases());
            binding.totalDeathsNumber.setText("" + globalInfoModel.getDeaths());
            binding.totalRecoveredNumber.setText("" + globalInfoModel.getRecovered());

        });

        globalInfoViewModel.getError().observe(this, aBoolean -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();

        });
    }


    //Run when you open the app and when you search for some thing and click search icon bun on single thread
    private void onLoadingSwipeRefresh() {
        binding.swipeRefreshLayout.post(this::loadData);
    }


}
