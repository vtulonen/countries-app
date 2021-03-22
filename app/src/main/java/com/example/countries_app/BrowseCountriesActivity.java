package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class BrowseCountriesActivity extends AppCompatActivity {

    RecyclerView mCountriesList;
    NameAdapter mAdapter;
    private static final String TAG = "BrowseCountriesActivity";
    RoomDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_countries);

        database = RoomDB.getInstance(this);
        List<CountryData> countries = database.countryDAO().getAll();
        Log.v("cdbrowse", countries.get(0).getName());

        mCountriesList = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCountriesList.setLayoutManager(layoutManager);
        String[] names = {"cfoo", "woo", countries.get(0).getName()}; // will be loaded from local storage later
        mAdapter = new NameAdapter(names);
        mCountriesList.setAdapter(mAdapter);

    }


}