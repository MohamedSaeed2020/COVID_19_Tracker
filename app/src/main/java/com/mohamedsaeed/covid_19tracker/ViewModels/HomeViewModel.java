package com.mohamedsaeed.covid_19tracker.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Repositories.HomeRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<CountryModel>> mCountryList;

    public HomeRepository homeRepository;

    public void init() {

        if (mCountryList != null) {
            return;
        }
        homeRepository = HomeRepository.getInstance();
        mCountryList = homeRepository.getCountries();

    }

    public LiveData<List<CountryModel>> getCountries() {
        return mCountryList;
    }


}
