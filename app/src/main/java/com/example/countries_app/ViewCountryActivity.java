package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.countries_app.utilities.MyRequestQueue;

import java.util.List;

public class ViewCountryActivity extends AppCompatActivity {

    RoomDB database;
    Country mCountry;
    ImageView mFlagImage;
    TextView mCapital;
    TextView mNativeName;
    TextView mRegion;
    TextView mPopulation;
    TextView mCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = RoomDB.getInstance(this);
        super.onCreate(savedInstanceState);
        String countryName = getIntent().getExtras().getString("countryName");
        setTitle(countryName);
        setContentView(R.layout.activity_view_country);
        mCountry = database.countryDAO().getOne(countryName);
        Log.v("flag", "flagurl: " + mCountry.getFlagUrl());

        mFlagImage = (ImageView) findViewById(R.id.iv_flag);
        SvgLoader.pluck()
                .with(this)
                //.setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(mCountry.getFlagUrl(), mFlagImage);

        mCapital = (TextView) findViewById(R.id.tv_capital);
        mCapital.setText(mCountry.getCapital());

        mNativeName = (TextView) findViewById(R.id.tv_nativeName);
        mNativeName.setText(mCountry.getNativeName());

        mRegion = (TextView) findViewById(R.id.tv_region);
        mRegion.setText(mCountry.getRegion() + ", " + mCountry.getSubregion());

        mPopulation = (TextView) findViewById(R.id.tv_population);
        mPopulation.setText(mCountry.getPopulation().toString());

        List<String> currenciesList = mCountry.getCurrencies();
        String currenciesString = "";
        for (String currency : currenciesList) {
            currenciesString += currency + "\n";
        }

        mCurrencies = (TextView) findViewById(R.id.tv_currencies);
        mCurrencies.setText(currenciesString);

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        SvgLoader.pluck().close();
    }

}