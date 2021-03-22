package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.countries_app.utilities.MyRequestQueue;
import com.example.countries_app.utilities.NetworkUtility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<CountryData> dataList = new ArrayList<>();
    RoomDB database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = RoomDB.getInstance(this);
        makeRestCountriesQuery();
        List<CountryData> countries = database.countryDAO().getAll();


        Log.v("cd", countries.get(0).display());

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
                    // TODO: save response to a local db (room)

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            //testing log response items
                            JSONObject country = response.getJSONObject(i);
                            String name = country.getString("name");
                            CountryData cd = new CountryData();
                            cd.setName(name);
                            database.countryDAO().insert(cd);
                            Log.v("countryname", name);
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
