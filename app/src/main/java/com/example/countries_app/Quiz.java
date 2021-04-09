package com.example.countries_app;

import android.content.Context;

import java.util.ArrayList;

/**
 * Quiz class that creates questions from random countries queried from RoomDB
 * Keeps track of -
 *      -users current score,
 *      -generated questions
 *      -question position,
 *      -if the game is over
 *      to help with creating "game logic" in QuizActivity
 */
public class Quiz {
    ArrayList<Question> questions = new ArrayList<>();
    private int score = 0;
    private int currentQuestionPos = 0;
    private boolean gameOver = false;
    private final int mAmountOfQuestions;
    RoomDB database;

    public Quiz(Context ctx, int amountOfQuestions, ArrayList<String> regions) {
        database = RoomDB.getInstance(ctx);
        mAmountOfQuestions = amountOfQuestions;

        while (questions.size() < mAmountOfQuestions) {
            Question newQuestion = generateQuestion(regions);
            if(!containsQuestion(questions, newQuestion.getAskedName())) { // If the questions is not already in, add it
                questions.add(newQuestion);
            } // else loop again
        }
    }

    /**
     *
     * @param regions user selected regions included in the quiz
     *                - used by countryDAO to determine which regions to include in random query of a country
     * @return Question object
     */
    public Question generateQuestion(ArrayList<String> regions) {
        ArrayList<Country> options = new ArrayList<Country>();

        while (options.size() < 4) {
            Country randomCountry = database.countryDAO()
                .getRandom(regions.get(0), regions.get(1), regions.get(2), regions.get(3), regions.get(4));

            // Check if options do not have the random country in the already
            if (!containsCountry(options, randomCountry.getName())) {
                options.add(randomCountry);
            } // else loop again
        }
        Question question = new Question(options);

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

    // Helpers to make sure no duplicates are added to the quiz
    public boolean containsCountry(final ArrayList<Country> countries, final String name){
        return countries.stream().anyMatch(country -> country.getName().equals(name));
    }

    public boolean containsQuestion(final ArrayList<Question> countries, final String name){
        return countries.stream().anyMatch(country -> country.getAskedName().equals(name));
    }
}
