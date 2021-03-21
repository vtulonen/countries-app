package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;

import com.android.volley.Request;

import com.android.volley.toolbox.JsonArrayRequest;

import com.example.countries_app.utilities.MyRequestQueue;
import com.example.countries_app.utilities.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class BrowseCountriesActivity extends AppCompatActivity {

    RecyclerView mCountriesList;
    NameAdapter mAdapter;
    private static final String TAG = "BrowseCountriesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_countries);
        makeRestCountriesQuery();


        mCountriesList = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCountriesList.setLayoutManager(layoutManager);
        String[] names = {"cfoo", "woo"}; // will be loaded from local storage later
        mAdapter = new NameAdapter(names);
        mCountriesList.setAdapter(mAdapter);

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
                    // TODO: save response to a local db (room)
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            //testing log response items
                            JSONObject country = response.getJSONObject(i);
                            String name = country.getString("name");
                            Log.v("countryname", name);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                Log.e(TAG, error.getMessage());
                });
        MyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}