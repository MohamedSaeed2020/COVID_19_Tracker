package com.mohamedsaeed.covid_19tracker.Models;

public class GlobalInfoModel {

    private int cases,deaths, recovered;

    public GlobalInfoModel(int cases, int deaths, int recovered) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }


    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
}
