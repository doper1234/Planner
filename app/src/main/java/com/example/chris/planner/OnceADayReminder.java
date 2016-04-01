package com.example.chris.planner;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by Chris on 27/03/2016.
 */
public class OnceADayReminder {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public OnceADayReminder(Context context){
        new AlertDialog.Builder(context)
                .setTitle("Delete everything?")
                .setMessage("Are you sure you want to erase your phone?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, null);
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, OnceADayReminder.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        60 * 1000, alarmIntent);
//

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);

// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                10000, alarmIntent);


    }
    public class SampleBootReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                OnceADayReminder onceADayReminder = new OnceADayReminder(context);
            }
        }
    }




}
