package com.example.strprojects.reactiontime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.example.strprojects.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReactionTimeCount {

    public static final int NUMBER_VIEW_GREEN_BUTTON_INDEX = 0;
    public static final int NUMBER_VIEW_YELLOW_BUTTON_INDEX = 1;
    public static final int NUMBER_VIEW_RED_BUTTON_INDEX = 2;
    public static final int NUMBER_VIEW_BLUE_BUTTON_INDEX = 3;

    private static final int TIME_TO_GONE_BUTTON = 1300;

    private static final int TOTAL_QUANTITY_TO_SHOW = 10;

    private Button[] buttons;
    private FloatingActionButton floatActButton;
    private Context context;
    private int height, width;
    private List<ButtonTimes> buttonTimesList;
    private ReactionTimeCountTimer timer;

    ExecutorService executorService;
    private ReadWriteLock lock;
    boolean running;


    private Runnable runnable;

    private int[] numberOfClicks;
    private int[] numberOfViews;
    private int[] colors;

    private static final String TAG = "ReactionTimeCount";

    private ButtonTimes currentCapturedTime;

    public static interface ReactionTimerCountListener{
        void countFinish(List<ButtonTimes> buttonTimesList, int[] numberViews, int[] numberOfClicks);
    }

    private ReactionTimerCountListener listener;

    public ReactionTimeCount(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ReactionTimeCount(Context context, FloatingActionButton floatActButton, ReactionTimeCountTimer timer, int height, int width,
                             ReactionTimerCountListener listener, Button[] buttons) {
        this.context = context;
        this.floatActButton = floatActButton;
        this.timer = timer;
        this.height = height;
        this.width = width;
        this.listener = listener;
        this.buttons = buttons;
        configCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void configCount() {
        currentCapturedTime = new ButtonTimes();
        lock = new ReentrantReadWriteLock();

        numberOfViews = new int[4];
        colors = new int[]{context.getResources().getColor(R.color.green, null), context.getResources().getColor(R.color.yellow, null),
                context.getResources().getColor(R.color.red, null), context.getResources().getColor(R.color.blue, null)};

        configButtons(buttons[NUMBER_VIEW_GREEN_BUTTON_INDEX], NUMBER_VIEW_GREEN_BUTTON_INDEX);
        configButtons(buttons[NUMBER_VIEW_YELLOW_BUTTON_INDEX], NUMBER_VIEW_YELLOW_BUTTON_INDEX);
        configButtons(buttons[NUMBER_VIEW_RED_BUTTON_INDEX], NUMBER_VIEW_RED_BUTTON_INDEX);
        configButtons(buttons[NUMBER_VIEW_BLUE_BUTTON_INDEX], NUMBER_VIEW_BLUE_BUTTON_INDEX);

        runnable = new Runnable() {
            @Override
            public void run() {
                int cont = 0;
                while(getChangeRunningValue(false, false) && cont < TOTAL_QUANTITY_TO_SHOW){
                    currentCapturedTime.setClickDate(null);
                    currentCapturedTime.setShowDate(new Date());
                    int displayedColorIndex = showButtonWithRandomColor();
                    countViews(displayedColorIndex);
                    try {
                        Thread.sleep(TIME_TO_GONE_BUTTON);
                        hiddenButton();
                        currentCapturedTime.setHiddenDate(new Date());
                        buttonTimesList.add(currentCapturedTime);
                        cont++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(listener != null){
                    listener.countFinish(buttonTimesList, numberOfViews, numberOfClicks);
                }
            }
        };
    }

    private void countViews(int displayedColorIndex) {
        switch (displayedColorIndex){
            case NUMBER_VIEW_GREEN_BUTTON_INDEX:
                numberOfViews[NUMBER_VIEW_GREEN_BUTTON_INDEX]++;
                break;
            case NUMBER_VIEW_YELLOW_BUTTON_INDEX:
                numberOfViews[NUMBER_VIEW_YELLOW_BUTTON_INDEX]++;
                break;
            case NUMBER_VIEW_RED_BUTTON_INDEX:
                numberOfViews[NUMBER_VIEW_RED_BUTTON_INDEX]++;
                break;
            case NUMBER_VIEW_BLUE_BUTTON_INDEX:
                numberOfViews[NUMBER_VIEW_BLUE_BUTTON_INDEX]++;
                break;
            default:
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void hiddenButton() {
        ((Activity)context).runOnUiThread(()->{
            floatActButton.setClickable(false);
            floatActButton.setVisibility(View.GONE);
            changeFloatActionButtonPosition(floatActButton);
        });
    }

    @SuppressLint("RestrictedApi")
    private int showButtonWithRandomColor() {
        int randomColorIndex = generateRandomInt(colors.length);
        ((Activity)context).runOnUiThread(() ->{
            changeFloatActionButtonPosition(floatActButton);
            floatActButton.setBackgroundTintList(ColorStateList.valueOf(colors[randomColorIndex]));
            floatActButton.setClickable(true);
            floatActButton.setVisibility(View.VISIBLE);
        });
        return randomColorIndex;
    }

    public void changeFloatActionButtonPosition(FloatingActionButton floatActButton){
        if(floatActButton != null){
            int positionX = generateRandomInt(width - floatActButton.getWidth() + 50);
            int positionY = generateRandomInt(height - floatActButton.getHeight() + 50);
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
        buttonTimesList = new ArrayList<>();
        numberOfViews = new int[4];
        numberOfClicks = new int[4];
        currentCapturedTime = new ButtonTimes();
        getChangeRunningValue(true, true);
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(runnable);
    }

    public void stopCount(){
        getChangeRunningValue(true, false);
        //Utils.shutdownAndAwaitTermination(executorService, 1);
    }

    public void configButtons(Button button, int index){
        if(button != null){
            button.setOnClickListener(v -> {
                Log.d(TAG, "onClick: click!");
                final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                if(toneGenerator != null) {
                    numberOfClicks[index]++;
                    currentCapturedTime.setClickDate(new Date());
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, ToneGenerator.MAX_VOLUME);
                    toneGenerator.release();
                }
            });
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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

    public List<ButtonTimes> getButtonTimesList() {
        return buttonTimesList;
    }

    public void setButtonTimesList(List<ButtonTimes> buttonTimesList) {
        this.buttonTimesList = buttonTimesList;
    }

    public int[] getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(int[] numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public ButtonTimes getCurrentCapturedTime() {
        return currentCapturedTime;
    }

    public void setCurrentCapturedTime(ButtonTimes currentCapturedTime) {
        this.currentCapturedTime = currentCapturedTime;
    }

    private boolean getChangeRunningValue(boolean change, boolean value){
        if (change){
            lock.writeLock().lock();
            try {
                running = value;
            }finally {
                lock.writeLock().unlock();
            }
        }
        boolean run;
        lock.readLock().lock();
        try {
            run = running;
        }finally {
            lock.readLock().unlock();
        }
        return run;
    }

}
