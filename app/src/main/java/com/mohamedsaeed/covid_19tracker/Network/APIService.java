package com.mohamedsaeed.covid_19tracker.Network;

import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Models.GlobalInfoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("countries")
    Call<List<CountryModel>> getCountries();

    @GET("countries/{country}")
    Call<CountryModel> getSpecificCountryInfo(@Path("country") String country);

    @GET("all")
    Call<GlobalInfoModel> getGlobalInfo();

}
