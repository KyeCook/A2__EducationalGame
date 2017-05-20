package com.example.kyecook.educationalgame;


import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_SCORE;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_USER;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_TABLE_NAME;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import twitter4j.Query;


public class HighscoresActivity extends AppCompatActivity {

    ListView highscoresList;
    ArrayList<HighScore> highscoresArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        Cursor cursor = getAllPersons();

        highscoresList = (ListView) findViewById(R.id.highscoresList);
        highscoresArray = new ArrayList<>();

        while(cursor.moveToNext()){
            String user = cursor.getString(cursor.getColumnIndex(HIGHSCORES_COLUMN_USER));
            int score = cursor.getInt(cursor.getColumnIndex(HIGHSCORES_COLUMN_SCORE));

            highscoresArray.add(new HighScore(score, user));

        }

        highscoresList.setAdapter(new ArrayAdapter<>(this.getApplicationContext(), R.layout.text_view , highscoresArray));
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

    @Override
    public void onBackPressed() {
    }
}
