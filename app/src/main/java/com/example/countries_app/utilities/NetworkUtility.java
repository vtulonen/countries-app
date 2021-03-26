package com.example.countries_app.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.ErrorListener;

public class NetworkUtility {
    final static String RESTCOUNTRIES_BASE_URL = "https://restcountries.eu/rest/v2";
    final static String PARAM_QUERY = "q";
    final static String PARAM_FIELDS = "fields";

    /**
     * Builds the URL used to query restcountries.
     *
     * @param service The path keyword for api call.
     * @param fields  specified fields to be called for.
     * @return The URL to use to fetch countries data from restcountries API.
     */

    // https://restcountries.eu/rest/v2/all?fields=name;capital;currencies

    public static URL buildUrl(String service, String[] fields) {
        String fieldsToString = String.join(";", fields);
        Uri builtUri = Uri.parse(RESTCOUNTRIES_BASE_URL).buildUpon()
                .appendPath(service)
                .appendQueryParameter(PARAM_QUERY, service)
                .appendQueryParameter(PARAM_FIELDS, fieldsToString)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


}


