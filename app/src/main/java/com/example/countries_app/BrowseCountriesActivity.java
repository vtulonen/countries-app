package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
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

        List<String> countryNames = database.countryDAO().getAllNames();
        Log.v("names", countryNames.toString());
        Country testcountry = database.countryDAO().getOne("Namibia");
        Log.v("testcountry", String.valueOf(testcountry.getPopulation()));


        mCountriesList = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCountriesList.setLayoutManager(layoutManager);
        mAdapter = new NameAdapter(countryNames);
        mCountriesList.setAdapter(mAdapter);

    }


}