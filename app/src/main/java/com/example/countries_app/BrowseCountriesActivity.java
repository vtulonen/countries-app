
package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.SearchView;

import java.util.List;


public class BrowseCountriesActivity extends AppCompatActivity implements NameAdapter.ListItemClickListener {

    RecyclerView mCountries;
    NameAdapter mAdapter;
    List<String> mCountryNameList;

    private static final String TAG = "BrowseCountriesActivity";
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_countries);

        database = RoomDB.getInstance(this);

        mCountryNameList = database.countryDAO().getAllNames();

        mCountries = (RecyclerView) findViewById(R.id.rv_country_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCountries.setLayoutManager(layoutManager);
        mAdapter = new NameAdapter(mCountryNameList, this);
        mCountries.setAdapter(mAdapter);

    }

    @Override
    public void onListItemClick(String clickedItemText) {
        Intent i = new Intent(this, ViewCountryActivity.class);
        i.putExtra("countryName", clickedItemText);
        startActivity(i);
    }

    /**
     * Create searchView in toolbar to filter countries
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.actionbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem menuPresenter = menu.findItem(R.id.action_menu_presenter);


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


}