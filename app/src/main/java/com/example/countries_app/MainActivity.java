package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.countries_app.utilities.MyRequestQueue;
import com.example.countries_app.utilities.NetworkUtility;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Country> dataList = new ArrayList<>();
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = RoomDB.getInstance(this);

        // Query api and populate db if db does not exist yet
        if (database.countryDAO().tableSize() == 0) {
            makeRestCountriesQuery(); // TODO: make this query timed? to check if data has changed
        }
    }

    public void openBrowseCountries(View view) {
        Intent i = new Intent(this, BrowseCountriesActivity.class);
        this.startActivity(i);
    }

    private void makeRestCountriesQuery() {
        Log.v("click", "clicked");
        String[] fields = {"name", "capital"};
        URL url =  NetworkUtility.buildUrl("all", fields);
        Log.v("url", url.toString());
        parseQuery(url.toString());
    }

    private void parseQuery(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject countryObj = response.getJSONObject(i);
                            String name = countryObj.getString("name");
                            Country country = new Country();
                            country.setName(name);

                            // Inset country to db
                            database.countryDAO().insert(country);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e("err", error.getMessage());
        });
        MyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}
