package com.example.countries_app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.countries_app.utilities.MyRequestQueue;
import com.example.countries_app.utilities.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Base app class - onCreate is run when application starts
 * Creates RoomDB instance and populates it with parsed data from api request
 * Also creates a notification channel
 */

public class App extends Application {
    RoomDB database;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        database = RoomDB.getInstance(this);

        // Query api and populate db if db does not exist yet
        if (database.countryDAO().tableSize() == 0) { // TODO: button for user to update db with new api call
            makeRestCountriesQuery();
        }
    }

    /**
     *  Channel for notifications
     */
    public static final String NOTICATION_ID = "quiz_complete";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTICATION_ID, "Quiz Complete", importance);
            channel.setDescription("Notifies user for a completed quiz");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     *  Create a query with specified fields passed to buildUrl method
     *  fetch the data from url with parseRequestQuery
     */
    private void makeRestCountriesQuery() {
        String[] fields = {}; // empty for all fields
        URL url =  NetworkUtility.buildUrl("all", fields);
        parseRequestQuery(url.toString());
    }

    /**
     * Url request is passed to Volley -> MyRequestQue
     * Response is parsed: country name, capital, nativeName, region, subregion, flagUrl, population, area, currencies and latitude & longitude
     * Mentioned data is saved to a Country class object which is then saved to RoomDB using CountryDAO (data access object)
     * @param url API url to fetch country data
     *
     */
    private void parseRequestQuery(String url) {
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
