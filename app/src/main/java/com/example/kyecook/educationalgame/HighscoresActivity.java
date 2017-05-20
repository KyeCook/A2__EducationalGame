package com.example.kyecook.educationalgame;


import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_SCORE;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_COLUMN_USER;
import static com.example.kyecook.educationalgame.EducationalGameDatabase.HIGHSCORES_TABLE_NAME;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


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
}
