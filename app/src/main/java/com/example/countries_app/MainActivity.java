package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.countries_app.utilities.MyRequestQueue;
import com.example.countries_app.utilities.NetworkUtility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RoomDB database;

    // TODO: on swipe open next country? -viewcountry activity
    // TODO: styles - all
    // TODO: capital quiz
    Calendar mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = RoomDB.getInstance(this);
        //database.countryDAO().deleteTable();
        Log.v("ts", String.valueOf(database.countryDAO().tableSize()));
        // Query api and populate db if db does not exist yet
        if (database.countryDAO().tableSize() == 0) {
            makeRestCountriesQuery(); // TODO: make this query timed? to check if data has changed
        }
       // sendFactNotification();
    }

    public void sendFactNotification() {
        Country randomCountry = database.countryDAO().getRandom();
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "daily_fact")
                .setSmallIcon(R.drawable.ic_search_icon)
                .setContentTitle("Daily Country Fact:")
                .setContentText(randomCountry.getName() + "'s capital is " + randomCountry.getCapital() + ". Click for more!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

          notificationManager.notify(1, builder.build());
    }

    public void openBrowseCountries(View view) {
        Intent i = new Intent(this, BrowseCountriesActivity.class);
        this.startActivity(i);
    }

    public void openQuizSettings(View view) {
        Intent i = new Intent(this, QuizSettingsActivity.class);
        this.startActivity(i);
    }

    private void makeRestCountriesQuery() {
        Log.v("click", "clicked");
        String[] fields = {}; // empty for all fields
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
                            String capital = countryObj.getString("capital");
                            String nativeName = countryObj.getString("nativeName");
                            String region = countryObj.getString("region");
                            String subregion = countryObj.getString("subregion");
                            String flagUrl = countryObj.getString("flag");
                            int population = countryObj.optInt("population", 0);
                            int area = countryObj.optInt("area", 0);

                            JSONArray currenciesArr = countryObj.getJSONArray("currencies");
                            List<String> currencies = new ArrayList<String>(1);
                            for (int j = 0; j < currenciesArr.length(); j++) {
                                JSONObject curObj = currenciesArr.getJSONObject(j);
                                currencies.add(curObj.getString("name"));
                            }

                            JSONArray latlngArr = countryObj.getJSONArray("latlng");
                            List<String> latlng = new ArrayList<>(2);
                            for (int k = 0; k <latlngArr.length() ; k++) {
                                latlng.add(latlngArr.getString(k));
                            }

                            Country country = new Country();
                            country.setName(name);
                            country.setNativeName(nativeName);
                            country.setSubregion(subregion);
                            country.setRegion(region);
                            country.setCurrencies(currencies);
                            country.setCapital(capital);
                            country.setPopulation(population);
                            country.setFlagUrl(flagUrl);
                            country.setLatlng(latlng);


                            country.setArea(area);

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
