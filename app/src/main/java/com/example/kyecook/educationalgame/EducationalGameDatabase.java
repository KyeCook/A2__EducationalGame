package com.example.kyecook.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Kye Cook
 * This class stores the relevant functions and information regarding the local database used in
 * storing received SMS messages.
 */

public class EducationalGameDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 11;
    private static final String DATABASE_NAME = "highscores.db";
    public static final String HIGHSCORES_TABLE_NAME = "highscores";
    static final String HIGHSCORES_COLUMN_UUID = "uuid";
    public static final String HIGHSCORES_COLUMN_USER = "user";
    public static final String HIGHSCORES_COLUMN_SCORE = "score";

    public static final String QUESTIONS_TABLE_NAME = "questions";
    static final String QUESTIONS_COLUMN_UUID = "uuid";
    public static final String QUESTIONS_COLUMN_QUESTION = "question";
    public static final String QUESTIONS_COLUMN_ANSWER = "correctAnswer";


    public EducationalGameDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + QUESTIONS_TABLE_NAME + "( _id integer primary key autoincrement, " +
                QUESTIONS_COLUMN_UUID + ", " +
                QUESTIONS_COLUMN_QUESTION + ", " +
                QUESTIONS_COLUMN_ANSWER + ") ");

        db.execSQL("create table " + HIGHSCORES_TABLE_NAME + "( _id integer primary key autoincrement, " +
                HIGHSCORES_COLUMN_UUID + ", " +
                HIGHSCORES_COLUMN_USER + ", " +
                HIGHSCORES_COLUMN_SCORE + ")");

        ArrayList<String> questionsArray = new ArrayList<>();

        questionsArray.add("Can Gorillas catch human colds and other illnesses? ");
        questionsArray.add("A newborn Chinese water deer is so small it can almost be held in the palm of the hand?");
        questionsArray.add("Ostriches can run faster than horses, and the males can roar like lions?");


        ArrayList<String> correctAnswersArray = new ArrayList<>();

        correctAnswersArray.add("TRUE");
        correctAnswersArray.add("FALSE");
        correctAnswersArray.add("TRUE");
//        String[] correctAnswersArray = {"TRUE", "FALSE", "TRUE"};


        for(int i = 0; i < questionsArray.size(); i++){
            ContentValues insertValues = new ContentValues();


            insertValues.put(QUESTIONS_COLUMN_QUESTION, questionsArray.get(i));
            insertValues.put(QUESTIONS_COLUMN_ANSWER, correctAnswersArray.get(i));

            db.insert(QUESTIONS_TABLE_NAME, null, insertValues);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORES_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}