package com.mohamedsaeed.covid_19tracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mohamedsaeed.covid_19tracker.Models.CountryModel;
import com.mohamedsaeed.covid_19tracker.R;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCountryAdapter extends ArrayAdapter<CountryModel> {

    private List<CountryModel> countries;
    private List<CountryModel> filteredCountries = new ArrayList<>();


    public AutoCompleteCountryAdapter(@NonNull Context context, @NonNull List<CountryModel> countryList) {
        super(context, 0, countryList);
        this.countries = countryList;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new countryFilter(this, countries);
    }

    @Override
    public int getCount() {
        return filteredCountries.size();
    }

    @Override
    public CountryModel getItem(int position) {
        return filteredCountries.get(position);
    }


    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CountryModel country = filteredCountries.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());


        convertView = inflater.inflate(R.layout.auto_complete_layout, parent, false);
        TextView textViewName = convertView.findViewById(R.id.countryName);
        textViewName.setText(country.getCountry());


        return convertView;
    }

    private class countryFilter extends Filter {


        AutoCompleteCountryAdapter adapter;
        List<CountryModel> originalList;
        List<CountryModel> filteredList;

        countryFilter(AutoCompleteCountryAdapter adapter, List<CountryModel> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CountryModel country : originalList) {
                    if (country.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(country);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredCountries.clear();
            adapter.filteredCountries.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }

    }


}
