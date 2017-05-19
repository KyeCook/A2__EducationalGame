package com.example.kyecook.educationalgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kyeco on 19/05/2017.
 */

public class AnswerDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "answers.db";
    public static final String ANSWERS_TABLE_NAME = "highscores";
    static final String ANSWERS_COLUMN_UUID = "uuid";
    public static final String ANSWERS_COLUMN_QUESTION = "question";
    public static final String ANSWERS_COLUMN_ANSWER__RIGHT= "correct answer";
    public static final String ANSWERS_COLUMN_ANSWER__WRONG= "wrong answer";


    public AnswerDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ANSWERS_TABLE_NAME + "( _id integer primary key autoincrement, " +
                ANSWERS_COLUMN_UUID + ", " +
                ANSWERS_COLUMN_QUESTION + ", " +
                ANSWERS_COLUMN_ANSWER__RIGHT + ", " +
                ANSWERS_COLUMN_ANSWER__WRONG + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ANSWERS_TABLE_NAME);
        onCreate(db);
    }
}
