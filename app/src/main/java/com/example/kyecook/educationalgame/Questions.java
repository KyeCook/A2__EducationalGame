package com.example.kyecook.educationalgame;

/**
 * Created by kyeco on 20/05/2017.
 */

public class Questions {
    private String question;
    private String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }


    Questions(String qu, String ar){

        question = qu;
        answer = ar;

    }

    public String toString(){
        return question + answer;
    }

}
