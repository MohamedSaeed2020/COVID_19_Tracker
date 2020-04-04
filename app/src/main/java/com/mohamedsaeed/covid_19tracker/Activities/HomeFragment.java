package com.mohamedsaeed.covid_19tracker.Activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.mohamedsaeed.covid_19tracker.Adapters.AutoCompleteCountryAdapter;
import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.Network.APIService;
import com.mohamedsaeed.covid_19tracker.Network.Client;
import com.mohamedsaeed.covid_19tracker.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private AutoCompleteTextView countrySearch;
    private APIService apiService;
    private List<CountryModel> stringList;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //init views
        countrySearch = view.findViewById(R.id.search_country_Et);
        Button search = view.findViewById(R.id.searchBtn);
        stringList = new ArrayList<>();

        //create api service
        apiService = Client.getClient("https://coronavirus-19-api.herokuapp.com/").create(APIService.class);
        apiService.getCountries()
                .enqueue(new Callback<List<CountryModel>>() {
                    @Override
                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                        assert response.body() != null;
                        stringList.addAll(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_LONG).show();

                    }
                });


        //Setting up the adapter for AutoSuggest
       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_dropdown_item_1line, stringList);
                countrySearch.setAdapter(arrayAdapter);
        */


        AutoCompleteCountryAdapter adapter = new AutoCompleteCountryAdapter(Objects.requireNonNull(getContext()), stringList);
        countrySearch.setAdapter(adapter);
        countrySearch.setThreshold(1); //will start working from first character
        countrySearch.setOnItemClickListener((parent, view12, position, id) -> {

            /* This method will give you the position after filtering (Wrong)*/
            /*CountryModel country= (CountryModel) parent.getItemAtPosition(position);
            countrySearch.setText(country.getCountry());*/

            /*This method will give you the package name (the CountryModel object as a name) (Error) */
            /*String countryName = Objects.requireNonNull(parent.getItemAtPosition(position)).toString();
            countrySearch.setText(countryName);*/

            String countryName = Objects.requireNonNull(adapter.getItem(position)).getCountry();
            countrySearch.setText(countryName);


        });


        //handle search button click listener
        search.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(countrySearch.getText().toString().trim())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.enter_country_name), Toast.LENGTH_SHORT).show();
                return;
            }
            String country_name = countrySearch.getText().toString().trim();
            countrySearch.setText("");
            loadCountryInfo(country_name);

        });

        return view;
    }

    private void loadCountryInfo(String country_name) {
        apiService.getSpecificCountryInfo(country_name)
                .enqueue(new Callback<CountryModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(getActivity(), SpecifCountryActivity.class);
                            assert response.body() != null;
                            intent.putExtra("totalCases", "" + response.body().getCases());
                            intent.putExtra("totalDeaths", "" + response.body().getDeaths());
                            intent.putExtra("todayCases", "" + response.body().getTodayCases());
                            intent.putExtra("todayDeaths", "" + response.body().getTodayDeaths());
                            intent.putExtra("totalRecovered", "" + response.body().getRecovered());
                            intent.putExtra("stillActive", "" + response.body().getActive());
                            intent.putExtra("search", country_name);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<CountryModel> call, Throwable t) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_result_matching), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
