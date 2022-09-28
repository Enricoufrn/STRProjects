package com.example.strprojects.reactiontime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.strprojects.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReactionTimeCount {

    public static final int NUMBER_VIEW_GREEN_BUTTON_INDEX = 0;
    public static final int NUMBER_VIEW_YELLOW_BUTTON_INDEX = 1;
    public static final int NUMBER_VIEW_RED_BUTTON_INDEX = 2;
    public static final int NUMBER_VIEW_BLUE_BUTTON_INDEX = 3;

    private FloatingActionButton floatActButton;
    private Context context;
    private int height, width;
    private List<ButtonTimes> buttonTimesList;
    private ReactionTimeCountTimer timer;
    private Runnable runnable;
    private int[] numberOfViews;
    private int[] colors;
    private static final String TAG = "ReactionTimeCount";

    private ButtonTimes currentCapturedTime;

    public ReactionTimeCount(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ReactionTimeCount(Context context, FloatingActionButton floatActButton, ReactionTimeCountTimer timer, int height, int width) {
        this.context = context;
        this.floatActButton = floatActButton;
        this.timer = timer;
        this.height = height;
        this.width = width;
        configCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void configCount() {
        currentCapturedTime = new ButtonTimes();
        numberOfViews = new int[4];
        colors = new int[]{context.getResources().getColor(R.color.green, null), context.getResources().getColor(R.color.yellow, null),
                context.getResources().getColor(R.color.red, null), context.getResources().getColor(R.color.blue, null)};
        configFloatActionButton(floatActButton);
        runnable = new Runnable() {
            @Override
            public void run() {
                currentCapturedTime.setShowDate(new Date());
                int displayedColorIndex = showButtonWithRandomColor();
                countViews(displayedColorIndex);
                hiddenButton();
                currentCapturedTime.setHiddenDate(new Date());
            }
        };
    }

    private void countViews(int displayedColorIndex) {
    }

    private void hiddenButton() {

    }

    @SuppressLint("RestrictedApi")
    private int showButtonWithRandomColor() {
        int randomColorIndex = generateRandomInt(colors.length);
        changeFloatActionButtonPosition(floatActButton);
        floatActButton.setBackgroundColor(colors[randomColorIndex]);
        floatActButton.setVisibility(View.VISIBLE);
        return randomColorIndex;
    }

    public void changeFloatActionButtonPosition(FloatingActionButton floatActButton){
        if(floatActButton != null){
            int positionX = generateRandomInt(width - floatActButton.getWidth());
            int positionY = generateRandomInt(height - floatActButton.getHeight());
            Log.d(TAG, "changeFloatActionButtonPosition: random coordinates generated: x = " + positionX + " | y = " + positionY);
            floatActButton.setX((float)positionX);
            floatActButton.setY((float)positionY);
        }
    }

    public void setFloatActButtonPosition(FloatingActionButton floatActButton, float x, float y){
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

    public void initCount(){

    }

    public void stopCount(){

    }

    public void configFloatActionButton(FloatingActionButton floatActButton){
        if(floatActButton != null){
            floatActButton.setOnClickListener(v -> {
                Log.d(TAG, "onClick: click!");
                final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
//                if(dateList != null && toneGenerator != null) {
//                    dateList.add(new Date());
//                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, ToneGenerator.MAX_VOLUME);
//                    toneGenerator.release();
//                }
            });
        }
    }

    public List<ButtonTimes> getButtonTimesList() {
        return buttonTimesList;
    }

    public void setDateList(List<ButtonTimes> buttonTimesList) {
        this.buttonTimesList = buttonTimesList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setButtonTimesList(List<ButtonTimes> buttonTimesList) {
        this.buttonTimesList = buttonTimesList;
    }

    public ReactionTimeCountTimer getTimer() {
        return timer;
    }

    public void setTimer(ReactionTimeCountTimer timer) {
        this.timer = timer;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
