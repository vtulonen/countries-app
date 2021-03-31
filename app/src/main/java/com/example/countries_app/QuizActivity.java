package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    RoomDB database;
    ArrayList<String> mRegions;
    final int MAX_REGIONS = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        database = RoomDB.getInstance(this);
        mRegions = getIntent().getExtras().getStringArrayList("regions");
        while (mRegions.size() < MAX_REGIONS) {
            mRegions.add("placeholder");
        }

        Log.v("reg", mRegions.toString());
        TextView tw = findViewById(R.id.tw_question);
        tw.setText(mRegions.toString());
        generateQuestion();
        generateQuestion();
        generateQuestion();
        generateQuestion();
    }

    public void generateQuestion() {
        ArrayList<Country> options = new ArrayList<Country>();

        while (options.size() < 4) {
            Country randomCountry = database.countryDAO()
                    .getRandom(mRegions.get(0), mRegions.get(1), mRegions.get(2), mRegions.get(3), mRegions.get(4));
            if (!options.contains(randomCountry)) {
                options.add(randomCountry);
            }
        }

        Question question = new Question(options);
        Log.e("question", "asnwer: " + question.getAnswer().getCapital());
       // Log.e("question", question.getOptions().toString());


    }

}