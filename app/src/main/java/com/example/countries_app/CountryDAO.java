package com.example.countries_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Data Access Objects - methods to access RoomDB countries table with SQLIte queries
 */
@Dao
public interface CountryDAO {
    // query
    @Insert(onConflict = REPLACE)
    void insert(Country country);

    //Delete query
    @Delete
    void delete(Country country);

    //Delete all
    @Query("DELETE FROM countries")
    void deleteTable();


    //Update
    @Query("UPDATE countries SET name = :sName WHERE ID = :sID")
    void update(int sID, String sName);

    //Get all data
    @Query("SELECT * FROM countries")
    List<Country> getAll();

    //Get names
    @Query("SELECT name FROM countries")
    List<String> getAllNames();

    //Get specific country by name
    @Query("SELECT * from countries WHERE name = :sName")
    Country getOne(String sName);


    @Query("SELECT capital from countries")
    List<String> getcap();

    //Table size
    @Query("SELECT count(*) FROM countries")
    int tableSize();

    //Get Random
    @Query("SELECT * FROM countries ORDER BY RANDOM() LIMIT 1")
    Country getRandom();

    // Get random where region
    @Query("SELECT * FROM countries WHERE " +
            "region LIKE :reg_1 " +
            "OR :reg_2 " +
            "OR :reg_3 " +
            "OR :reg_4 " +
            "OR :reg_5 " +
            "ORDER BY RANDOM() LIMIT 1")
    Country getRandom(String reg_1, String reg_2, String reg_3, String reg_4, String reg_5);


}
