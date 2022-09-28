package com.example.strprojects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.strprojects.reactiontime.ReactionTimeCount;
import com.example.strprojects.reactiontime.ReactionTimeCountTimer;
import com.example.strprojects.utils.LoadingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatActButton;
    private Button runButton;
    private LinearLayout linearLayout;

    private ReactionTimeCount reactionTimeCount;
    private ReactionTimeCountTimer timer;
    private Dialog loadingDialog;
    private ReadWriteLock lock;

    private static final String TAG = "MainActivityLOG";
    private static final int DELAY = 1000;
    private static final int PERIOD = 2000;
    private static final int HEIGHT_INDEX = 0;
    private static final int WIDTH_INDEX = 1;

    private boolean running = false;
    private int linearLayoutHeight;
    private int linearLayoutWidth;
    private float initFloatButtonPositionX;
    private float initFloatButtonPositionY;

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
                reactionTimeCount = new ReactionTimeCount(floatActButton, finalDimensions[HEIGHT_INDEX], finalDimensions[WIDTH_INDEX]);
                timer = new ReactionTimeCountTimer(DELAY, PERIOD);
                loadingDialog.dismiss();
            });
        });
    }

    private void configButtons() {
        if(runButton != null){
            runButton.setOnClickListener(v -> {
                if(!running){
                    Log.d(TAG, "onClick: Start");
                    timer.initTimer(new TimerTask() {
                        @Override
                        public void run() {
                            reactionTimeCount.changeFloatActionButtonPosition();
                        }
                    });
                    running = true;
                    runButton.setText(getResources().getString(R.string.stop));
                }else{
                    Log.d(TAG, "onClick: Stop");
                    timer.stopTimer();
                    running = false;
                    reactionTimeCount.setFloatActButtonPosition(initFloatButtonPositionX, initFloatButtonPositionY);
                    runButton.setText(getResources().getString(R.string.start));
                }
            });
        }
    }

    private void findComponents(){
        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                getOrSetDimensions(true, linearLayout.getHeight(), linearLayout.getWidth());
                initFloatButtonPositionX = floatActButton.getTranslationX();
                initFloatButtonPositionY = floatActButton.getTranslationY();
            }
        });
        floatActButton = findViewById(R.id.float_action_button);
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

}