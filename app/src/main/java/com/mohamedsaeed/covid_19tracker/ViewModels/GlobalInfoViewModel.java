package com.mohamedsaeed.covid_19tracker.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohamedsaeed.covid_19tracker.Models.GlobalInfoModel;
import com.mohamedsaeed.covid_19tracker.Repositories.GlobalInfoRepositories;

public class GlobalInfoViewModel extends ViewModel {

    private MutableLiveData<GlobalInfoModel> mGlobalInfo;
    private MutableLiveData<Boolean> mError;


    public GlobalInfoRepositories globalInfoRepositories;

    public void init() {

        if (mGlobalInfo != null) {
            return;
        }
        globalInfoRepositories = GlobalInfoRepositories.getInstance();
        mGlobalInfo = globalInfoRepositories.loadStatisticsData();
        mError = globalInfoRepositories.onFailureData;

    }

    public LiveData<GlobalInfoModel> getStatistics() {
        return mGlobalInfo;
    }

    public LiveData<Boolean> getError() {
        return mError;
    }

}
