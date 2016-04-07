package com.example.chris.planner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import junit.runner.Version;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Version2 extends Activity {

    String dayOfTheWeek, dateOfTheMonth;
    private PendingIntent pendingIntent, pendingBootIntent;
    List<String> eventNames, eventFrequencies, eventDurations;
    HashMap<String, List<String>> childList;
    ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version2);
        TableData.TableInfo.EDITING = false;
        Intent alarmIntent = new Intent(Version2.this, UnfinishedEventsReminderReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Version2.this, 0, alarmIntent, 0);

        Intent bootIntent = new Intent(Version2.this, ResetFinishedEventsReceiver.class);
        pendingBootIntent = PendingIntent.getBroadcast(Version2.this, 0, bootIntent, 0);

        //startAlarmAtSpecificTime(12, 12, 1);
        startAlarmManager();
        startResetAlarmManager();
        createStartScreen();
        //setColours();

    }

    public void createStartScreen(){
        Button calendarButton, addButton, settingsButton, historyButton;
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset();
                setExpandableListEvents();
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
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView2);
        ll.removeAllViews();
        boolean allFinished = true;
        childList = new HashMap<>();
        eventNames = new ArrayList<>();
        eventFrequencies = new ArrayList<>();
        eventDurations = new ArrayList<>();
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished;
                int eventDuration;

                eventFinished = cr.getString(4);
                if(eventFinished.equalsIgnoreCase("no")){
                    eventName = cr.getString(0);
                    eventFrequency = cr.getString(1);
                    eventDuration = Integer.parseInt(cr.getString(2));
                    //new Event(this, eventName, eventFrequency, eventDuration, null, null,ll);
                    setExpandableListEvents(eventName, eventFrequency, Integer.toString(eventDuration));
//                    eventNames.add(eventName);
//                    eventFrequencies.add(eventFrequency);
//                    eventDurations.add(Integer.toString(eventDuration));
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

    private void setExpandableListEvents(String eventName, String eventFrequency, String eventDuration){
//
//        HashMap<String, List<String>> childList = new HashMap<>();
//        for (int i = 0; i < eventNames.size(); i ++){
//            List<String> deviceInfo = new ArrayList<>();
//            deviceInfo.add(eventFrequency.get(i));
//            deviceInfo.add(eventDuration.get(i));
//            deviceInfo.add("Edit");
//            deviceInfo.add("Finished");
//            deviceInfo.add("Subtract Time");
//            childList.put(eventNames.get(i), deviceInfo);
//            Log.d("New Event", eventNames.get(i));
//        }
//        ExpandableListView expandableListView = new ExpandableListView(this);
//        BluetoothExpandableListAdapter adapter = new BluetoothExpandableListAdapter(this, eventNames, childList);
//        Log.d(eventNames.toString(), childList.toString());
//        expandableListView.setAdapter(adapter);
//        ll.addView(expandableListView);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if(parent.getExpandableListAdapter().getChild(groupPosition, childPosition).equals("Finished")){
//                    DataBaseOperations dbo = new DataBaseOperations(Version2.this);
//                    dbo.updateEventFinished(dbo, (String)parent.getExpandableListAdapter().getGroup(groupPosition));
//                    setupTodaysEvents();
                    finishedAnEvent((String)parent.getExpandableListAdapter().getGroup(groupPosition));
                }
//                Toast.makeText(Version2.this, "You clicked " + parent.getExpandableListAdapter().getChild(groupPosition, childPosition) +
//                                                " on event " + parent.getExpandableListAdapter().getGroup(groupPosition), Toast.LENGTH_LONG).show(); ;
                return true;
            }
        });
        eventNames.add(eventName);
        eventFrequencies.add(eventFrequency);
        eventDurations.add(eventDuration);
        for (int i = 0; i < eventNames.size(); i ++){
            List<String> deviceInfo = new ArrayList<>();
            deviceInfo.add(eventFrequencies.get(i));
            if(Integer.parseInt(eventDurations.get(i)) > 0){
                deviceInfo.add("Time remaining: " + eventDurations.get(i));
            }
            deviceInfo.add("Edit");
            deviceInfo.add("Finished");
            deviceInfo.add("Subtract Time");
            deviceInfo.add("Move Event To Tomorrow(Once)");
            deviceInfo.add("Move Event To Tomorrow(Indefinitely)");
            childList.put(eventNames.get(i), deviceInfo);
        }
        BluetoothExpandableListAdapter adapter = new BluetoothExpandableListAdapter(this, this.eventNames, childList);
        expandableListView.setAdapter(adapter);
    }

    private void finishedAnEvent(final String title){
            new AlertDialog.Builder(this)
                    .setTitle("Event Finished")
                    .setMessage("Are you sure you've finished "+ title+"?")
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            DataBaseOperations dbo = new DataBaseOperations(Version2.this);
                            dbo.updateEventFinished(dbo, title);
                            setupTodaysEvents();
                            Toast.makeText(Version2.this, "You have finished an event!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
    }

    private void setExpandableListEvents(){
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView2);
     //   ExpandableListView expandableListView = new ExpandableListView(this);
//        ScrollView ll = (ScrollView) findViewById(R.id.mainScrollView);
        LinearLayout ll = (LinearLayout) findViewById(R.id.mainLinearLayout);
        ll.addView(expandableListView);
        HashMap<String, List<String>> childList = new HashMap<>();
        List<String> eventNames = new ArrayList<>();
        eventNames.add("Test Event One");
        eventNames.add("Event Test Two");

        for (int i = 0; i < eventNames.size(); i ++){
            List<String> deviceInfo = new ArrayList<>();
            deviceInfo.add("wednesday thursday");
            deviceInfo.add("time remaining: 45 minutes");
            deviceInfo.add("Edit");
            deviceInfo.add("Finished");
            deviceInfo.add("Subtract Time");
            childList.put(eventNames.get(i), deviceInfo);
        }
        BluetoothExpandableListAdapter adapter = new BluetoothExpandableListAdapter(this, eventNames, childList);
        expandableListView.setAdapter(adapter);
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

    public void startAlarmManager() {
        Calendar calendar = Calendar.getInstance();
        Calendar alarmCalender = calendar;
        alarmCalender.set(Calendar.HOUR_OF_DAY, 19);
        alarmCalender.set(Calendar.MINUTE, 30);
        alarmCalender.set(Calendar.SECOND,0);

//        long time24h = 24*60*60*1000;
//        long timeAt09_00 = ...; // calculate from now...
//        long timeAt11_30 = ...; // calculate from now...
//
//        alarmMgr1.setInexactRepeating(AlarmManager.RTC_WAKEUP, now ,        time24h, alarmIntent);
//        alarmMgr2.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt09_00, time24h, alarmIntent);
//        alarmMgr3.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt11_30, time24h, alarmIntent);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000*60*60*24;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), interval, pendingIntent);
        //Toast.makeText(this, "Alarm Set for 13:30", Toast.LENGTH_SHORT).show();
    }

    public void startResetAlarmManager(){
        Calendar calendar = Calendar.getInstance();
        Calendar alarmCalender = calendar;
        alarmCalender.set(Calendar.HOUR_OF_DAY, 23);
        alarmCalender.set(Calendar.MINUTE, 59);
        alarmCalender.set(Calendar.SECOND,0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 *60 * 24;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), interval, pendingBootIntent);
        //Toast.makeText(this, "Boot Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarmManager() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAlarmAtSpecificTime(int hour, int minute, int repeatEveryXMinutes) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at time hour:minute */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        /* Repeating on every x minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * repeatEveryXMinutes, pendingIntent);
    }
}
