package com.example.chris.planner;

/**
 * Created by Chris on 12/03/2016.
 */
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

public class AutoStart extends BroadcastReceiver
{
    Alarm alarm = new Alarm();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.SetAlarm(context);
        }
    }
}
