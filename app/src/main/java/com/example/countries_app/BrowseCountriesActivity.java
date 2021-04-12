
package com.example.countries_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.ProgressBar;
import android.widget.SearchView;

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
 * "Main activity" of the application that displays all country names in a list (recylcerView)
 * Includes onClick methods for clicking countries, search or starting a new quiz
 */
public class BrowseCountriesActivity extends AppCompatActivity implements NameAdapter.ListItemClickListener {

    private RecyclerView mCountries;
    private NameAdapter mAdapter;
    private List<String> mCountryNameList;
    private RoomDB database;
    private NameAdapter.ListItemClickListener mClickListener;
    private Context mCtx;
    private ProgressBar mLoadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_countries);
        mClickListener = this;
        mCtx = this;
        database = RoomDB.getInstance(this);
        mLoadingPB = (ProgressBar) findViewById(R.id.pb_loading);

        if (database.countryDAO().tableSize() == 0) {
            makeRestCountriesQuery();
        } else {
            setUpRecyclerView();
        }
    }

    /**
     * Opens ViewCountryActivity that displays data about the clicked country
     * @param clickedItemText text (country name) of the clicked item
     * countryName is passed as extra to the new activity
     */
    @Override
    public void onListItemClick(String clickedItemText) {
        Intent i = new Intent(this, ViewCountryActivity.class);
        i.putExtra("countryName", clickedItemText);
        startActivity(i);
    }

    /**
     * Create searchView in toolbar to filter countries, start a quiz
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.actionbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView sw = (SearchView) searchItem.getActionView();
        sw.setImeOptions(EditorInfo.IME_ACTION_DONE); // search icon to done icon
        sw.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); // prevent landscape layout to take full height of the screen
        sw.setIconifiedByDefault(false); // added to guarantee searchView expanding on click
        sw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Added handler to set focus on searchView after clicking
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                new Handler(Looper.myLooper()).post(() -> {
                    sw.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(sw.findFocus(), 0);
                });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return true;
    }


    public void openQuizSettings(MenuItem item) {
        Intent i = new Intent(this, QuizSettingsActivity.class);
        this.startActivity(i);
    }

    public void onUpdateClick(MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Update Countries")
                .setMessage("Would you like to update the countries with latest data?")
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        database.countryDAO().deleteTable();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })

                // null listener = nothing happens on click
                .setNegativeButton(R.string.btn_no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Sets up recylcerView
     * This is called in activity onCreate if the database is already set up
     * or after volleyQuery is returns response
     *
     */
    public void setUpRecyclerView() {
        // Names are fetched from RoomDB database
        mCountryNameList = database.countryDAO().getAllNames();
        mCountries = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mCtx);
        mCountries.setLayoutManager(layoutManager);
        mAdapter = new NameAdapter(mCountryNameList, mClickListener);
        mCountries.setAdapter(mAdapter); // TODO: while adapter is not set -> show loading icon
    }

    /**
     *  Create a query with specified fields passed to buildUrl method
     *  fetch the data from url with parseRequestQuery
     *  callback onSuccess after query completes to display fetched data
     */
    private void makeRestCountriesQuery() {
        mLoadingPB.setVisibility(View.VISIBLE);
        String[] fields = {}; // leave empty for all fields
        URL url =  NetworkUtility.buildUrl("all", fields);
        parseRequestQuery(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                mLoadingPB.setVisibility(View.INVISIBLE);
                setUpRecyclerView();
            }
        }, url.toString());
    }

    /**
     * Callback interface for volley request - cb function defined when calling parseRequestQuery
     */
    public interface VolleyCallBack {
        void onSuccess();
    }


    /**
     * Url request is passed to Volley -> MyRequestQue
     * Response is parsed: country name, capital, nativeName, region, subregion, flagUrl, population, area, currencies and latitude & longitude
     * Mentioned data is saved to a Country class object which is then saved to RoomDB using CountryDAO (data access object)
     * @param url API url to fetch country data
     *
     */
    private void parseRequestQuery(VolleyCallBack volleyCallBack, String url) {
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

                            database.countryDAO().insert(country);
                        }

                        volleyCallBack.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e("err", error.getMessage());
        });
        MyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}