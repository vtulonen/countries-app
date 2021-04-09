package com.example.countries_app;

import java.util.ArrayList;
import java.util.Random;

/**
 * Country quiz spesific question class about capitals
 * Creates a Question object from an array of Country objects
 */
public class Question {

    private Country asked;
    private ArrayList<Country> optionsCountries;
    private String askedCapital;
    private String askedName;
    private ArrayList<String> options = new ArrayList<>();

    /**
     * 4 countries passed as list are set as possible options
     * 1 is randomly selected to be the answer
     *
     * @param optionsCountries list of countries included in the question
     */
    public Question(ArrayList<Country> optionsCountries) {
        setOptionsCountries(optionsCountries);
        setAsked();
        setAskedCapital(asked); // Capital of the asked country - used to check if user guessed correct
        setAskedName(asked); // Name of the asked country - used to display to user when asking the question in quiz
        setOptions(); // List of capitals of the countries provided as options - displayed to user in quiz
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
