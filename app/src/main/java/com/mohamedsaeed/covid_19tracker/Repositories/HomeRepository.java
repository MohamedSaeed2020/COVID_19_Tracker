package com.mohamedsaeed.covid_19tracker.Repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Singleton pattern
 */

public class HomeRepository {

    private static HomeRepository instance;
    private final ArrayList<CountryModel> countryList = new ArrayList<>();


    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    // Pretend to get data from a webservice or online source
    public MutableLiveData<List<CountryModel>> getCountries() {

        getDataFromAPI();
        MutableLiveData<List<CountryModel>> data = new MutableLiveData<>();
        data.setValue(countryList);
        return data;
    }

    private void getDataFromAPI() {
        //create api service
        APIService apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        Single<List<CountryModel>> observable = apiService.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<List<CountryModel>> observer = new SingleObserver<List<CountryModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<CountryModel> countryModels) {
                countryList.addAll(countryModels);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //Toast.makeText(context, "Failed!", Toast.LENGTH_LONG).show();

            }
        };

        observable.subscribe(observer);
    }


}
