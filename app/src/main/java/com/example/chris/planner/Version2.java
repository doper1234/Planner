package com.example.chris.planner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Version2 extends Activity {

    String dayOfTheWeek, dateOfTheMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version2);
        TableData.TableInfo.EDITING = false;
        createStartScreen();
        setColours();

    }

    public void createStartScreen(){
        Button calendarButton, addButton, settingsButton, historyButton;
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        calendarButton = (Button)findViewById(R.id.calendarButtonMain);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Version2.this, CalendarActivity.class));
                //TableData.TableInfo.EDITING = false;
            }
        });
        addButton = (Button) findViewById(R.id.addButtonMain);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Version2.this, NewEventScreen.class));
            }
        });
        settingsButton = (Button) findViewById(R.id.settingsButtonMain);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Version2.this, SettingsActivity.class));
            }
        });
        historyButton = (Button) findViewById(R.id.historyButtonMain);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Version2.this, PreviousHistory.class));
            }
        });
        setToday();
        setupTodaysEvents();

    }

    private void setColours(){
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mainScreenLayout);
        rl.setBackgroundColor(Colours.backgroundColour);
        ScrollView sv = (ScrollView) findViewById(R.id.mainScrollView);
        sv.setBackgroundColor(Colours.foregroundColour);
        Button calendarButton, addButton, settingsButton, historyButton;
        Button testButton = (Button) findViewById(R.id.testButton);
        calendarButton = (Button)findViewById(R.id.calendarButtonMain);
        addButton = (Button) findViewById(R.id.addButtonMain);
        settingsButton = (Button) findViewById(R.id.settingsButtonMain);
        historyButton = (Button) findViewById(R.id.historyButtonMain);
        testButton.setBackgroundColor(Colours.buttonColour);
        calendarButton.setBackgroundColor(Colours.buttonColour);
        addButton.setBackgroundColor(Colours.buttonColour);
        settingsButton.setBackgroundColor(Colours.buttonColour);
        historyButton.setBackgroundColor(Colours.buttonColour);
    }

    private void setToday(){
        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
//        dateView = (TextView) findViewById(R.id.dateView);
//        dateView.setText(df.format(calobj.getTime()));
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        dayOfTheWeek = getDateInfo[0];
        String splitDate[] = getDateInfo[1].split(" ");
        dateOfTheMonth = splitDate[2];
    }

    public void setupTodaysEvents(){
        DataBaseOperations dbo = new DataBaseOperations(this);
        TextView noEvents = (TextView) findViewById(R.id.noEventsTextView);
        Cursor cr = dbo.getInformation(dbo, dayOfTheWeek, dateOfTheMonth);
        LinearLayout ll = (LinearLayout) findViewById(R.id.mainLinearLayout);
        ll.removeAllViews();
        boolean allFinished = true;
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished;
                int eventDuration;

                eventFinished = cr.getString(4);
                if(eventFinished.equalsIgnoreCase("no")){
                    eventName = cr.getString(0);
                    eventFrequency = cr.getString(1);
                    eventDuration = Integer.parseInt(cr.getString(2));
                    new Event(this, eventName, eventFrequency, eventDuration, ll);
                    allFinished = false;
                }

            }while(cr.moveToNext());
            if(allFinished){
                noEvents.setVisibility(View.VISIBLE);
                noEvents.setText("You have finished all events! Well done!");
            }else{
                noEvents.setVisibility(View.INVISIBLE);
            }

        }else{
            noEvents.setVisibility(View.VISIBLE);
            noEvents.setText("You have no events. Click add to add some!");
        }
    }


    private void resetEventsDuration(DataBaseOperations dbo){
        Cursor cr = dbo.getInformation(dbo);
        cr.moveToFirst();
        if(cr.moveToNext()){
            do{
                String eventName;
                eventName = cr.getString(0);
                dbo.resetDuration(dbo, eventName);
            }while(cr.moveToNext());
        }
    }

    public void reset(){
        DataBaseOperations dbo = new DataBaseOperations(this);
        dbo.resetFinished(dbo);
        resetEventsDuration(dbo);
        showNotification();
        setupTodaysEvents();
    }

    public void showNotification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.thursday))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
    }
}
