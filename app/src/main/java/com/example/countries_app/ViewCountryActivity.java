package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.List;

/**
 * Displayed data stored in roomDB of a given country
 * Button to show country on Maps
 * Finds the country from roomDB based on country name (putExtra from BrowseCountriesActivity onClick)
 */
public class ViewCountryActivity extends AppCompatActivity {

    RoomDB database;
    Country mCountry;
    ImageView mFlagImage;
    TextView mCapital;
    TextView mNativeName;
    TextView mRegion;
    TextView mPopulation;
    TextView mCurrencies;

    /**
     * Get country from database and populate textviews and image view with the country's data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = RoomDB.getInstance(this);
        super.onCreate(savedInstanceState);
        String countryName = getIntent().getExtras().getString("countryName");
        setTitle(countryName);
        setContentView(R.layout.activity_view_country);
        mCountry = database.countryDAO().getOne(countryName);
        mFlagImage = (ImageView) findViewById(R.id.iv_flag);
        /**
         * SvgLoader library used to display and svg image from url
         */
        SvgLoader.pluck()
                .with(this)
                //.setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(mCountry.getFlagUrl(), mFlagImage);

        mCapital = (TextView) findViewById(R.id.tv_capital);
        mCapital.setText(mCountry.getCapital());

        mNativeName = (TextView) findViewById(R.id.tv_nativeName);
        mNativeName.setText(mCountry.getNativeName());

        mRegion = (TextView) findViewById(R.id.tv_region);
        String regionsText = mCountry.getRegion() + ", " + mCountry.getSubregion();
        mRegion.setText(regionsText);

        mPopulation = (TextView) findViewById(R.id.tv_population);
        mPopulation.setText(String.valueOf(mCountry.getPopulation()));

        List<String> currenciesList = mCountry.getCurrencies();
        String currenciesString = "";
        for (String currency : currenciesList) {
            currenciesString += currency + "\n";
        }
        currenciesString = currenciesString.trim();


        mCurrencies = (TextView) findViewById(R.id.tv_currencies);
        mCurrencies.setText(currenciesString);

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        SvgLoader.pluck().close();
    }

    /**
     * Use country's latitude and longitude to construct gmmIntentUri for google Maps
     * Determine zoomLevel based on country's area
     * Open Google Maps with intent
     */
    public void openMapsClick(View view) {
        String coordinates =  String.join(",", mCountry.getLatlng());
        int zoomLevel = getZoomLevel(mCountry.getArea());
        Uri gmmIntentUri = Uri.parse("geo:" + coordinates + "?z=" + zoomLevel); // determines the location and zoom lvl where maps is opened at
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            Log.e("error", "Can't open Google Maps");
        }
    }

    // Determine zoom level for maps uri based on country area size
    private int getZoomLevel(final int area) {
        int zoomLevel = 0;

        if (area > 10000000) {
            zoomLevel = 2;
        } else if (area > 7000000) {
            zoomLevel = 3;
        } else if (area > 3000000) {
            zoomLevel = 5;
        } else if (area > 2000000) {
            zoomLevel = 6;
        } else {
            zoomLevel = 7;
        }

        return zoomLevel;
    }
}