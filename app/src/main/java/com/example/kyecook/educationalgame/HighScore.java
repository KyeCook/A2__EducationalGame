package com.example.kyecook.educationalgame;

/**
 * Created by kyecook on 18/05/2017.
 */

public class HighScore {
    private int score;
    private String name;

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    HighScore(int s, String n){
        score = s;
        name = n;
    }

}
