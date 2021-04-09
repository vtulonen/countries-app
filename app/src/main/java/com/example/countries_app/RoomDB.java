package com.example.countries_app;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.countries_app.utilities.Converters;

/**
 * RoomDB used to store country data
 * database is static so it won't be created for every new instance
 * getInstance is static + synchronized to prevent simultaneous write/read to db
 */

// db entities
@Database(entities = {Country.class}, version = 7, exportSchema = false)
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
