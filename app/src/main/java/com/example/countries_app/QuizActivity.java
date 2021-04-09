package com.example.countries_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Quiz is displayed in this activity - creates a quiz based on user input from previous QuizSettingsActivity
 * Contains the basic logic behind the quiz
 */
public class QuizActivity extends AppCompatActivity {

    ArrayList<String> mRegions;
    final int MAX_REGIONS = 5;
    int mQnum;
    TextView tw_question_value;
    List<Button> buttons;
    Quiz quiz;
    Button nextBtn;
    final int AMOUNT_OF_QUESTIONS = 10;

    /**
     * Creates quiz based on selected regions
     * Creates array of buttons that will be populated with options in displayQuestion();
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        final int[] OPTION_BUTTON_IDS = {
                R.id.btn_option_1,
                R.id.btn_option_2,
                R.id.btn_option_3,
                R.id.btn_option_4,
        };

        buttons = new ArrayList<Button>();
        for (int id : OPTION_BUTTON_IDS) {
            Button btn = (Button) findViewById(id);
            buttons.add(btn);
        }

        mRegions = getIntent().getExtras().getStringArrayList("regions");
        // fill mRegions with placeholder text for database query to work
        while (mRegions.size() < MAX_REGIONS) {
            mRegions.add("placeholder");
        }

        tw_question_value = findViewById(R.id.tw_question_value);

        quiz = new Quiz(this, AMOUNT_OF_QUESTIONS, mRegions);

        nextBtn = findViewById(R.id.btn_next);
        nextBtn.performClick(); // displayQuestion (first)

    }

    /**
     * Basic game logic here -> track score, display next question, disable/enable option & next buttons, as well as display win text
     */
    public void displayQuestion(View view) {

        if (quiz.isGameOver()) {
            sendQuizNotification();
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.container);
            //Hide all views in layout
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }

            // Display end of quiz textview and buttons
            TextView twEndMessage = findViewById(R.id.tw_end_of_quiz_message);
            String endMessage = "You scored " + quiz.getScore() + "/" + AMOUNT_OF_QUESTIONS;
            twEndMessage.setText(endMessage);
            twEndMessage.setVisibility(View.VISIBLE);
            findViewById(R.id.btn_retry).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_back_to_menu).setVisibility(View.VISIBLE);


        } else {
            mQnum = quiz.getCurrentQuestionPos();
            List<String> options = quiz.questions.get(mQnum).getOptions();
            tw_question_value.setText(quiz.questions.get(mQnum).getAskedName());
            for (int i=0; i<buttons.size(); i++) {
                buttons.get(i).setText(options.get(i)); // populate buttontexts with options
                buttons.get(i).setEnabled(true); // set back to enabled
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.orangepeel, null)); // reset after last question changed color
            }

            nextBtn.setVisibility(View.INVISIBLE); // hide next button until option is clicked
        }

    }

    // User guess
    public void onOptionButtonClick(View view) {
        Button clickedBtn = (Button) view;
        String btnText = clickedBtn.getText().toString();
        boolean isCorrect = checkAnswer(btnText);

        //Disable option buttons after clicking one
        for (int i=0; i<buttons.size(); i++) {
            buttons.get(i).setEnabled(false);
            if (buttons.get(i).getText() == quiz.questions.get(mQnum).getAskedCapital()) {
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.button_correct, null));
            }
        }

        if (isCorrect) {
            quiz.increaseScore();
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.button_correct, null));
        } else {
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.button_wrong, null ));
        }

        quiz.increaseCurrentQuestionPos(); // track next question

        //Show 'next' button
        nextBtn.setVisibility(View.VISIBLE);

        //Waiting user to press next to go continue
    }


    public void onMenuBtnClick(View view) { // Back to BrowseCountriesActivity after quiz done
        Intent i = new Intent(this, BrowseCountriesActivity.class);
        startActivity(i);
    }

    public void onNewBtnClick(View view) { // Goes back to quizSettingsView
        onBackPressed();
    }

    public boolean checkAnswer(String answer) {
        return answer.equals(quiz.questions.get(mQnum).getAskedCapital());
    }

    /**
     * After quiz is complete send a notification congratulating user
     */
    public void sendQuizNotification() {
        Intent i = new Intent(this, QuizSettingsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "quiz_complete")
                .setSmallIcon(R.drawable.ic_search_icon)
                .setContentTitle("Great! Quiz Complete")
                .setContentText("Click here for a new round")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }

}