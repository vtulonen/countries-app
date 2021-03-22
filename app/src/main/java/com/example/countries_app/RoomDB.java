package com.example.countries_app;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// db entities
@Database(entities = {CountryData.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    // Db instance
    private static RoomDB database;

    // Db name
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            // If null, initialize
            database = Room.databaseBuilder(context.getApplicationContext()
                    , RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    // Create DAO
    public abstract CountryDAO countryDAO();
}
