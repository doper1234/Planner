package com.example.chris.planner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Chris on 29/03/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("I'm alive", "right?");
        Toast.makeText(context, "Boot Alarm Set", Toast.LENGTH_SHORT).show();
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, ResetFinishedEventsReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 8000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            Toast.makeText(context, "Boot Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }
}
