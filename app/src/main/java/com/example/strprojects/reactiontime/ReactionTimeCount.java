package com.example.strprojects.reactiontime;

import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class ReactionTimeCount {

    private FloatingActionButton floatActButton;
    private int height, width;

    private static final String TAG = "ReactionTimeCount";

    public ReactionTimeCount(){

    }

    public ReactionTimeCount(FloatingActionButton floatActButton ,int height, int width) {
        this.floatActButton = floatActButton;
        this.height = height;
        this.width = width;
    }

    public void changeFloatActionButtonPosition(){
        if(floatActButton != null){
            int positionX = generateRandomInt(width - floatActButton.getWidth());
            int positionY = generateRandomInt(height - floatActButton.getHeight());
            Log.d(TAG, "changeFloatActionButtonPosition: random coordinates generated: x = " + positionX + " | y = " + positionY);
            floatActButton.setX((float)positionX);
            floatActButton.setY((float)positionY);
        }
    }

    public void setFloatActButtonPosition(float x, float y){
        if(floatActButton != null){
            floatActButton.setX(x);
            floatActButton.setY(y);
        }
    }

    private int generateRandomInt(int upperBound){
        Random random = new Random();
        return random.nextInt(upperBound);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public FloatingActionButton getFloatActButton() {
        return floatActButton;
    }

    public void setFloatActButton(FloatingActionButton floatActButton) {
        this.floatActButton = floatActButton;
    }
}
