package com.example.kyecook.educationalgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import static com.example.kyecook.educationalgame.EducationalGameDatabase.QUESTIONS_TABLE_NAME;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.QUESTIONS_COLUMN_QUESTION;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.QUESTIONS_COLUMN_ANSWER;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameScreenActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };



    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private SharedPreferences preferences;

    private int questionNumber;

    private ArrayList<Questions> questionsArrayList;

//    private ArrayAdapter<String> questionsArrayList;
    private TextView questionText;
    private Button answerTrue;
    private Button answerFalse;
    private String userName;
    private TextView userScore;
    private int userCurrentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_screen);

        /* ##################  Android Fullscreen Activity code  ############################# */

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.returnButtonHandler).setOnTouchListener(mDelayHideTouchListener);

        /* ################################################################################## */

        /* getIntent.getIntExtra("name_of_extra", -1); */

        userName = (String) getIntent().getExtras().get("userName");
        userScore = (TextView) findViewById(R.id.scoreText);
        userCurrentScore = 0;

        userScore.setText(userName + " : " + userCurrentScore);

        answerTrue = (Button) findViewById(R.id.true_handler);
        answerTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionsArrayList.get(questionNumber).getAnswer().equals("TRUE")){
                    userCurrentScore +=100;
                    nextQuestion();
                } else {
                    nextQuestion();
                }
            }
        });

        answerFalse = (Button) findViewById(R.id.false_handler);
        answerFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionsArrayList.get(questionNumber).getAnswer().equals("FALSE")){
                    userCurrentScore +=100;
                    nextQuestion();
                } else {
                    nextQuestion();
                }
            }
        });

        questionText = (TextView)findViewById(R.id.questionText);
        questionNumber = 0;
//        questionsArrayList = new ArrayList<>(this, android.R.layout.simple_list_item_1);

        Cursor cursor = getAllQuestions();

        questionsArrayList = new ArrayList<>();


        while(cursor.moveToNext()){
            String question = cursor.getString(cursor.getColumnIndex(QUESTIONS_COLUMN_QUESTION));
            String answer = cursor.getString(cursor.getColumnIndex(QUESTIONS_COLUMN_ANSWER));


            questionsArrayList.add(new Questions(question,answer));

            System.out.println(questionsArrayList);

        }

        questionText.setText(questionsArrayList.get(questionNumber).getQuestion());

        preferences = getSharedPreferences("gamePreferences", MODE_PRIVATE);

        Button returnButton = (Button) findViewById(R.id.returnButtonHandler);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

    }

    public Cursor getAllQuestions() {
        return MainActivity.mDatabase.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME, null);
    }

//    Method to change to next question
    public void nextQuestion(){

        userScore.setText(userName + " : " + userCurrentScore);

        if(questionNumber == questionsArrayList.size() - 1){
//            Takes user to High Scores Screen
            Intent intent = new Intent(this, HighscoresActivity.class);
            startActivity(intent);

        }
        else {
            questionNumber ++;
            questionText.setText(questionsArrayList.get(questionNumber).getQuestion());
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_highscores){
            Intent intent = new Intent(this, HighscoresActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void gameHandler(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
    }
}
