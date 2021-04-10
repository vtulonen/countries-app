package com.example.countries_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 *  RoomDB
 *  Table entity to store Country objects
 */
//Table name
@Entity(tableName = "countries")
public class Country implements Serializable {

    // Columns
    // Id
    @PrimaryKey(autoGenerate = true)
    private int ID;

    // Name
    @ColumnInfo(name = "name")
    private String name;

    // Native Name
    @ColumnInfo(name = "nativeName")
    private String nativeName;

    // Capital
    @ColumnInfo(name = "capital")
    private String capital;

    // Region
    @ColumnInfo(name = "region")
    private String region;

    // Subregion
    @ColumnInfo(name = "subregion")
    private String subregion;

    // Population
    @ColumnInfo(name = "population")
    private int population;

    // Flag to svg url
    @ColumnInfo(name = "flagUrl")
    private String flagUrl;

    // Currencies
    @ColumnInfo(name = "currencies")
    private List<String> currencies;

    // Latitude & longitude
    @ColumnInfo(name = "latlng")
    private List<String> latlng;

    //Area
    @ColumnInfo(name = "area")
    private int area;

    //Getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }


    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public List<String> getLatlng() { return latlng; }

    public void setLatlng(List<String> latlng) { this.latlng = latlng; }

    public int getArea() { return area;  }

    public void setArea(int area) { this.area = area; }


}
