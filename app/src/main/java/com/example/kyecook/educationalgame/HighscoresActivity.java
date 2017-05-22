package com.example.kyecook.educationalgame;


import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_SCORE;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_USER;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_TABLE_NAME;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class HighscoresActivity extends AppCompatActivity {

    private ImageButton facebookButton;
    private ImageButton twitterButton;
    private Button playAgain;
    ListView highscoresList;
    ArrayList<HighScore> highscoresArray;


    private static final int AUTHENTICATE = 1;
    Twitter twitter = TwitterFactory.getSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        Cursor cursor = getAllPersons();

        highscoresList = (ListView) findViewById(R.id.highscoresList);
        highscoresArray = new ArrayList<>();

        facebookButton = (ImageButton) findViewById(R.id.postFacebook);
        twitterButton = (ImageButton) findViewById(R.id.postTwitter);
        playAgain = (Button)findViewById(R.id.playAgain_handler);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgainHandler();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HighscoresActivity.this, "High Score Posting to facebook is coming " +
                        "soon\n\nStay Tuned!!", Toast.LENGTH_SHORT).show();
            }
        });



        while (cursor.moveToNext()) {
            String user = cursor.getString(cursor.getColumnIndex(HIGHSCORES_COLUMN_USER));
            int score = cursor.getInt(cursor.getColumnIndex(HIGHSCORES_COLUMN_SCORE));

//            Adds database values into ArrayList

            highscoresArray.add(new HighScore(score, user));

//            todo add in code to sort Highscores from Highest to Lowest

//            Removes Score if list exceeds 5 positions
            if(highscoresArray.size() > 5){
                highscoresArray.remove(5);
            }

        }

        highscoresList.setAdapter(new ArrayAdapter<>(this.getApplicationContext(), R.layout.text_view, highscoresArray));
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

//        Ensures this activity only runs if Twitter API has been authenticated.
        if (requestCode == AUTHENTICATE && resultCode == RESULT_OK) {
            Background.run(new Runnable() {
                @Override
                public void run() {
//                    Gets Twitter API access tokens to enable authentication

                    String token = data.getStringExtra("access token");
                    String secret = data.getStringExtra("access token secret");
                    AccessToken accessToken = new AccessToken(token, secret);
                    twitter.setOAuthAccessToken(accessToken);

                    try {
//                        Lets developer know that Twitter Post was successful
                        System.out.println("PUBLISHING TO TWITTER");

//                        Lets user know Twitter post of HighScores was successful
                        Toast.makeText(HighscoresActivity.this, "Publishing High score information " +
                                "to Twitter", Toast.LENGTH_SHORT).show();

                        twitter.updateStatus(highscoresArray.toString());

                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                                Lets developer know of error
                                System.out.println(e.toString());
                            }
                        });
                    }
                }
            });
        }
    }


    public Cursor getAllPersons() {
        return MainActivity.mDatabase.rawQuery("SELECT * FROM " + HIGHSCORES_TABLE_NAME, null);

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

    public void authenticateTwitterHandler(View view) {

        Intent intent = new Intent(this, Authenticate.class);
        startActivityForResult(intent, AUTHENTICATE);

    }

    public void playAgainHandler(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
