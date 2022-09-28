package com.example.strprojects.reactiontime;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ReactionTimeCountTimer {

    private Timer timer;
    private int delay, period;
    private boolean timerCanceled = false;

    private static final String TAG = "ReactionTimerCountTimer";

    public ReactionTimeCountTimer(int delay, int period) {
        this.delay = delay;
        this.period = period;
    }

    public void initTimer(TimerTask timerTask){
        if(timer == null || timerCanceled) {
            Log.d(TAG, "initTimer: Create a new timer!");
            timer = new Timer();
            timer.schedule(timerTask, delay, period);
        }
    }

    public void stopTimer(){
        if(timer != null){
            Log.d(TAG, "stopTimer: timer canceled!");
            timer.cancel();
            timerCanceled = true;
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
