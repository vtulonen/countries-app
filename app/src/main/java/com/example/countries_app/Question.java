package com.example.countries_app;

import java.util.ArrayList;
import java.util.Random;

public class Question {
    public Question(ArrayList<Country> options) {
        setOptions(options);
        setAnswer();
    }

    Country answer;
    ArrayList<Country> options;

    // Get & set
    public Country getAnswer() {
        return answer;
    }

    public void setAnswer() {
        this.answer = options.get(getRandomBetween(0,3)); // set random question from options
    }

    public ArrayList<Country> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Country> options) {
        this.options = options;
    }

    // Helper
    public int getRandomBetween(int MIN, int MAX) {
        int random = new Random().nextInt((MAX - MIN) + 1) + MIN;
        return random;
    }

}
