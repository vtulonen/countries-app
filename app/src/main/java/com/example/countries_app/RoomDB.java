package com.example.countries_app;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.countries_app.utilities.Converters;

// db entities
@Database(entities = {Country.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
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
