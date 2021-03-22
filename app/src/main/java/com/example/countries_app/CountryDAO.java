package com.example.countries_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CountryDAO {
    // query
    @Insert(onConflict = REPLACE)
    void insert(CountryData countryData);

    //Delete query
    @Delete
    void delete(CountryData countryData);

    //Delete all
    @Delete
    void reset(List<CountryData> countryData);

    //Update
    @Query("UPDATE countries SET name = :sName WHERE ID = :sID")
    void update(int sID, String sName);

    //Get all data
    @Query("SELECT * FROM countries")
    List<CountryData> getAll();

}
