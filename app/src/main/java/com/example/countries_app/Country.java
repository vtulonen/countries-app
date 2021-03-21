package com.example.countries_app;

public class Country {
    private String name;
    private String capital;

    Country(String name, String capital) {
        this.name = name;
        this.capital = capital;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

}
