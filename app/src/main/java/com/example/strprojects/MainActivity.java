package com.example.strprojects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.strprojects.dialogs.ShowReactionTimeAvg;
import com.example.strprojects.reactiontime.ButtonTimes;
import com.example.strprojects.reactiontime.ReactionTimeCount;
import com.example.strprojects.reactiontime.ReactionTimeCountTimer;
import com.example.strprojects.dialogs.LoadingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainActivity extends AppCompatActivity implements ReactionTimeCount.ReactionTimerCountListener {

    private FloatingActionButton floatingActionButton;

    private Button runButton;
    private LinearLayout linearLayout;

    private ReactionTimeCount reactionTimeCount;
    private ReactionTimeCountTimer timer;
    private Dialog loadingDialog;
    private ReadWriteLock lock;

    private static final String TAG = "MainActivityLOG";
    private static final int DELAY = 1000;
    private static final int PERIOD = 1500;
    private static final int HEIGHT_INDEX = 0;
    private static final int WIDTH_INDEX = 1;

    private boolean running = false;
    private int linearLayoutHeight;
    private int linearLayoutWidth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findComponents();
        configButtons();
        LoadingDialog dialog = new LoadingDialog(this);
        loadingDialog = dialog.getLoadingDialog();
        lock = new ReentrantReadWriteLock();
        Log.d(TAG, "onCreate: display height: " + linearLayoutHeight + "| width: " + linearLayoutWidth);
        loadingDialog.show();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() ->{
            int[] dimensions;
            do {
                Log.d(TAG, "display height: " + linearLayoutHeight + "| width: " + linearLayoutWidth);
                dimensions = getOrSetDimensions(false, 0, 0);
            }while (dimensions[HEIGHT_INDEX] == 0 || dimensions[WIDTH_INDEX] == 0);
            int[] finalDimensions = dimensions;
            runOnUiThread(() ->{
                Log.d(TAG, "display height: " + linearLayoutHeight + "| width: " + linearLayoutWidth);
                timer = new ReactionTimeCountTimer(DELAY, PERIOD);
                reactionTimeCount = new ReactionTimeCount(MainActivity.this,
                        floatingActionButton, timer, finalDimensions[HEIGHT_INDEX], finalDimensions[WIDTH_INDEX], MainActivity.this);
                loadingDialog.dismiss();
            });
        });
    }

    @SuppressLint("RestrictedApi")
    private void configButtons() {
        if(runButton != null){
            runButton.setOnClickListener(v -> {
                if(!running){
                    startOrStop(true);
                    reactionTimeCount.initCount();
                }
            });
        }
    }

    @SuppressLint("RestrictedApi")
    private void startOrStop(boolean start){
        String buttonText, logMessage;
        boolean runningValue;
        if(start){
            buttonText = getResources().getString(R.string.stop);
            runningValue = true;
            logMessage = "Start!";
        }else{
            buttonText = getResources().getString(R.string.start);
            runningValue = false;
            logMessage = "Stop!";
        }
        Log.d(TAG, "startOrStop: " + logMessage);
        running = runningValue;
        runButton.setText(buttonText);
    }

    private void findComponents(){
        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                getOrSetDimensions(true, linearLayout.getHeight(), linearLayout.getWidth());
            }
        });
        floatingActionButton = findViewById(R.id.float_action_button);
        runButton = findViewById(R.id.run_button);
    }

    private int[] getOrSetDimensions(boolean set, int height, int width){
        if (set){
            lock.writeLock().lock();
            try {
                linearLayoutHeight = height;
                linearLayoutWidth = width;
            }finally {
                lock.writeLock().unlock();
            }
        }
        int[] dimensions = new int[2];
        lock.readLock().lock();
        try {
            dimensions[HEIGHT_INDEX] = linearLayoutHeight;
            dimensions[WIDTH_INDEX] = linearLayoutWidth;
        }finally {
            lock.readLock().unlock();
        }
        return dimensions;
    }

    @Override
    public void countFinish(List<ButtonTimes> buttonTimesList, int[] numberViews) {
        startOrStop(false);
        ShowReactionTimeAvg showReactionTimeAvg = new ShowReactionTimeAvg(this, buttonTimesList);
        showReactionTimeAvg.show(getSupportFragmentManager(), "REACTION_TIME_RESULT_DIALOG");
    }
}