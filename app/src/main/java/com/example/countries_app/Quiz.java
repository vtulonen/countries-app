package com.example.countries_app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class Quiz {

    ArrayList<Question> questions = new ArrayList<>();
    int score = 0;
    int currentQuestionPos = 0;
    boolean gameOver = false;
    RoomDB database;
    int mAmountOfQuestions;

    public Quiz(Context ctx, int amountOfQuestions, ArrayList<String> regions) {
        database = RoomDB.getInstance(ctx);
        mAmountOfQuestions = amountOfQuestions;

        for (int i = 0; i < mAmountOfQuestions; i++) {
            questions.add(generateQuestion(regions));
        }
    }

    public Question generateQuestion(ArrayList<String> regions) {
        ArrayList<Country> options = new ArrayList<Country>();

        while (options.size() < 4) {
            Country randomCountry = database.countryDAO()
                    .getRandom(regions.get(0), regions.get(1), regions.get(2), regions.get(3), regions.get(4));
            if (!options.contains(randomCountry)) { // TODO: THIS IS NOT WORKING
                options.add(randomCountry);
            }
        }
        Question question = new Question(options);
        //Log.e("question", "What is the capital of " + question.getAskedName() + "?");
        //Log.e("question", "Options: " + question.getOptions().toString());
        return question;
    }


    //Getters
    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getCurrentQuestionPos() {
        return currentQuestionPos;
    }

    public void increaseCurrentQuestionPos() {
        this.currentQuestionPos += 1;
        if( currentQuestionPos == mAmountOfQuestions) {
            setGameOver(true);
        }
    }

    // Setters
    public void increaseScore() {
        this.score += 1;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


}
