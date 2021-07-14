package com.mohamedsaeed.covid_19tracker.Network;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Models.GlobalInfoModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("countries")
    Single<List<CountryModel>> getCountries();

    @GET("countries/{country}")
    Single<CountryModel> getSpecificCountryInfo(@Path("country") String country);

    @GET("all")
    Single<GlobalInfoModel> getGlobalInfo();

}
