package com.example.chris.planner;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chris on 12/03/2016.
 */
public class ResetScheduleTimer {

    private static Timer mainTimer;
    private static TimerTask timerTask;
    private final Handler handler = new Handler();


    public void start(final MainActivity ma, int one){
        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    ma.reset();
                    Toast.makeText(ma, "Resetted", Toast.LENGTH_LONG).show();
                }
            };
        }
        if(mainTimer == null){
            mainTimer = new Timer();
            mainTimer.schedule(timerTask, 1000);
        }
    }

    public void start(final MainActivity ma) {
        //set a new Timer
        mainTimer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(ma);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        mainTimer.schedule(timerTask, 200000, 200000); //

    }
    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (mainTimer != null) {

            mainTimer.cancel();
            mainTimer = null;
        }

    }
    public void initializeTimerTask(final MainActivity ma) {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {

                    public void run() {
                        ma.reset();
                        //Toast.makeText(ma, "Resetted", Toast.LENGTH_LONG).show();
                    }

                });
            }

        };
    }
}
