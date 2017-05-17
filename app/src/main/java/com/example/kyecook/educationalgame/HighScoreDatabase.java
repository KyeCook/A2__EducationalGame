package com.example.kyecook.educationalgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kye Cook
 * This class stores the relevant functions and information regarding the local database used in
 * storing received SMS messages.
 */

public class HighScoreDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "highscores.db";
    public static final String HIGHSCORES_TABLE_NAME = "highscores";
    static final String HIGHSCORES_COLUMN_UUID = "uuid";
    public static final String HIGHSCORES_COLUMN_USER = "user";
    public static final String HIGHSCORES_COLUMN_SCORE = "score";


    public HighScoreDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + HIGHSCORES_TABLE_NAME + "( _id integer primary key autoincrement, " +
                HIGHSCORES_COLUMN_UUID + ", " +
                HIGHSCORES_COLUMN_USER + ", " +
                HIGHSCORES_COLUMN_SCORE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORES_TABLE_NAME);
        onCreate(db);
    }
}