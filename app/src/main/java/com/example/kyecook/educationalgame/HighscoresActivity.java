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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class HighscoresActivity extends AppCompatActivity {

    private ImageButton facebookButton;
    ListView highscoresList;
    ArrayList<HighScore> highscoresArray;

    private TextView textView;
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

        textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());

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

            highscoresArray.add(new HighScore(score, user));

        }

        highscoresList.setAdapter(new ArrayAdapter<>(this.getApplicationContext(), R.layout.text_view, highscoresArray));
    }


    public void authorise(View view) {
        Intent intent = new Intent(this, Authenticate.class);
        startActivityForResult(intent, AUTHENTICATE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == AUTHENTICATE && resultCode == RESULT_OK) {
            Background.run(new Runnable() {
                @Override
                public void run() {
                    String token = data.getStringExtra("access token");
                    String secret = data.getStringExtra("access token secret");
                    AccessToken accessToken = new AccessToken(token, secret);
                    twitter.setOAuthAccessToken(accessToken);

                    Query query = new Query("@twitterapi");
                    QueryResult result;
                    try {
                        twitter.updateStatus("everything is fine!");
                        result = twitter.search(query);
                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(e.toString());
                            }
                        });
                        return;
                    }

                    // convert tweets into text
                    final StringBuilder builder = new StringBuilder();
                    for (Status status : result.getTweets()) {
                        builder.append(status.getUser().getScreenName())
                                .append(" said ")
                                .append(status.getText())
                                .append("\n");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(builder.toString().trim());
                        }
                    });
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
        } else if(id == R.id.action_playAgain) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void authenticateTwitterHandler(View view) {

        Intent intent = new Intent(this, Authenticate.class);
        startActivity(intent);

        try {

            twitter.updateStatus("everything is fine!");
        } catch (Exception e){

        }

    }

    @Override
    public void onBackPressed() {
    }
}
