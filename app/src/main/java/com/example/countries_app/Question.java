package com.example.countries_app;

import java.util.ArrayList;
import java.util.Random;

public class Question {

    private Country asked;
    private ArrayList<Country> optionsCountries;

    public String askedCapital;
    public String askedName;
    public ArrayList<String> options = new ArrayList<>();

    public Question(ArrayList<Country> optionsCountries) {
        setOptionsCountries(optionsCountries);
        setAsked();
        setAskedCapital(asked);
        setAskedName(asked);
        setOptions();
    }


    // Get & set
    private void setAsked() {
        this.asked = optionsCountries.get(getRandomBetween(0,3)); // set random question from options
    }

    private void setOptionsCountries(ArrayList<Country> options) {
        this.optionsCountries = options;
    }

    public String getAskedCapital() {
        return askedCapital;
    }

    private void setAskedCapital(Country asked) {
        this.askedCapital = asked.getCapital();
    }

    public String getAskedName() {
        return askedName;
    }

    private void setAskedName(Country asked) {
        this.askedName = asked.getName();
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    private void setOptions() {
       for ( Country country : optionsCountries) {
            options.add(country.getCapital());
       }
    }

    // Helper
    private int getRandomBetween(int MIN, int MAX) {
        int random = new Random().nextInt((MAX - MIN) + 1) + MIN;
        return random;
    }



}
