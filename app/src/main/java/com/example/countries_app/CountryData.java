package com.example.countries_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Table name
@Entity(tableName = "countries")
public class CountryData implements Serializable {
    // Id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    // name col
    @ColumnInfo(name = "name")
    private String name;

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

    public String display() {
        return name;
    }
}
