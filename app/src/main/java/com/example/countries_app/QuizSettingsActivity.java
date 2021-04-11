package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Displays different regions as checkboxes for user to define what to include in the quiz.
 * Button to start the quiz by starting new QuizActivity
 */
public class QuizSettingsActivity extends AppCompatActivity {

    ArrayList<String> mSelectedRegions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.select_regions));
        setContentView(R.layout.activity_quiz_settings);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        CheckBox cb = findViewById(view.getId());
        String cbText = cb.getText().toString();
        if (checked) {
            mSelectedRegions.add(cbText);
        } else {
            mSelectedRegions.remove(cbText);
        }

        Button startButton = findViewById(R.id.btn_start);
        if (mSelectedRegions.isEmpty()) {
            startButton.setVisibility(View.INVISIBLE);
        } else {
            startButton.setVisibility(View.VISIBLE);
        }
    }


    public void startQuiz(View view) {
        Intent i = new Intent(this, QuizActivity.class);
        i.putExtra("regions", mSelectedRegions);
        startActivity(i);
    }
}