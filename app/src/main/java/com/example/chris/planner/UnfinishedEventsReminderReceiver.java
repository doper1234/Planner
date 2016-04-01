package com.example.chris.planner;

/**
 * Created by Chris on 29/03/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class UnfinishedEventsReminderReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Toast.makeText(context, "Boot", Toast.LENGTH_SHORT).show();
//        }
        this.context = context;
        Toast.makeText(context, "Unfinished events reminder", Toast.LENGTH_SHORT).show();
        sendUnfinishedEventsReminder();
    }

    public void sendUnfinishedEventsReminder(){
        showNotification();
    }

    public void showNotification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0); ?
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                //.setContentIntent(pi) ?
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
    }
}
