package com.mohamedsaeed.covid_19tracker.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohamedsaeed.covid_19tracker.Models.GlobalInfoModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Singleton pattern
 */
public class GlobalInfoRepositories {
    APIService apiService;
    private static GlobalInfoRepositories instance;
    public MutableLiveData<GlobalInfoModel> onResponseData = new MutableLiveData<>();
    public MutableLiveData<Boolean> onFailureData = new MutableLiveData<>();


    public static GlobalInfoRepositories getInstance() {
        if (instance == null) {
            instance = new GlobalInfoRepositories();
        }
        return instance;
    }

    // Pretend to get data from a webservice or online source
    public MutableLiveData<GlobalInfoModel> loadStatisticsData() {
        //create api service
        apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        Single<GlobalInfoModel> observable =apiService.getGlobalInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

       SingleObserver<GlobalInfoModel> observer=new SingleObserver<GlobalInfoModel>() {
           @Override
           public void onSubscribe(@NonNull Disposable d) {

           }

           @Override
           public void onSuccess(@NonNull GlobalInfoModel globalInfoModel) {
               onResponseData.setValue(globalInfoModel);
           }

           @Override
           public void onError(@NonNull Throwable e) {
               onFailureData.setValue(true);

           }
       };

       observable.subscribe(observer);
       return onResponseData;

    }


}
