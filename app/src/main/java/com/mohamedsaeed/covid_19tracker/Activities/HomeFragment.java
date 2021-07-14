package com.mohamedsaeed.covid_19tracker.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mohamedsaeed.covid_19tracker.Adapters.AutoCompleteCountryAdapter;
import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;
import com.mohamedsaeed.covid_19tracker.R;
import com.mohamedsaeed.covid_19tracker.ViewModels.HomeViewModel;
import com.mohamedsaeed.covid_19tracker.databinding.FragmentHomeBinding;

import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    APIService apiService;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();
        AutoCompleteCountryAdapter adapter = new AutoCompleteCountryAdapter(Objects.requireNonNull(getContext()), Objects.requireNonNull(homeViewModel.getCountries().getValue()));

        homeViewModel.getCountries().observe(Objects.requireNonNull(getActivity()), nicePlaces -> adapter.notifyDataSetChanged());

        binding.searchCountryEt.setAdapter(adapter);
        binding.searchCountryEt.setThreshold(1); //will start working from first character
        binding.searchCountryEt.setOnItemClickListener((parent, view12, position, id) -> {
            String countryName = Objects.requireNonNull(adapter.getItem(position)).getCountry();
            binding.searchCountryEt.setText(countryName);
        });


        //handle search button click listener
        binding.searchBtn.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(binding.searchCountryEt.getText().toString().trim())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.enter_country_name), Toast.LENGTH_SHORT).show();
                return;
            }
            String country_name = binding.searchCountryEt.getText().toString().trim();
            binding.searchCountryEt.setText("");
            loadCountryInfo(country_name);

        });

        return view;
    }

    private void loadCountryInfo(String country_name) {
        apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        Single<CountryModel> observable=apiService.getSpecificCountryInfo(country_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<CountryModel> observer=new SingleObserver<CountryModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull CountryModel countryModel) {
                Intent intent = new Intent(getActivity(), SpecificCountryActivity.class);
                intent.putExtra("totalCases", "" + countryModel.getCases());
                intent.putExtra("totalDeaths", "" + countryModel.getDeaths());
                intent.putExtra("todayCases", "" + countryModel.getTodayCases());
                intent.putExtra("todayDeaths", "" + countryModel.getTodayDeaths());
                intent.putExtra("totalRecovered", "" + countryModel.getRecovered());
                intent.putExtra("stillActive", "" + countryModel.getActive());
                intent.putExtra("search", country_name);
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_result_matching), Toast.LENGTH_SHORT).show();
            }
        };
        observable.subscribe(observer);
    }

}
