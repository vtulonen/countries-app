package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class BrowseCountriesActivity extends AppCompatActivity implements NameAdapter.ListItemClickListener {

    RecyclerView mCountriesList;
    NameAdapter mAdapter;
    private Toast mToast;
    private static final String TAG = "BrowseCountriesActivity";
    RoomDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Browse Countries");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_countries);

        database = RoomDB.getInstance(this);

        List<String> countryNames = database.countryDAO().getAllNames();

        mCountriesList = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCountriesList.setLayoutManager(layoutManager);
        mAdapter = new NameAdapter(countryNames, this);
        mCountriesList.setAdapter(mAdapter);

    }

    @Override
    public void onListItemClick(String clickedItemText) {
        if (mToast != null) {
            mToast.cancel();
        }
        Log.v("click", "testclick");

        String toastMessage = "Clicked item: " + clickedItemText;
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

        Intent i = new Intent(this, ViewCountryActivity.class);
        i.putExtra("countryName", clickedItemText);
        startActivity(i);
    }

}