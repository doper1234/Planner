package com.example.chris.planner;

/**
 * Created by Chris on 11/04/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class TodaysEventsFragment extends Fragment {

    String dayOfTheWeek, dateOfTheMonth;
    List<String> eventNames, eventFrequencies, eventDurations;
    HashMap<String, List<String>> childList;
    ListView expandableListView;
    Context ctx;
    ViewGroup rootView;
    SlideScreenActivity slideScreenActivity;

    public TodaysEventsFragment(Context context, SlideScreenActivity ssa){
        ctx = context;
        slideScreenActivity = ssa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.todays_events_fragment, container, false);

        setupTodaysEvents();

        return rootView;
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.todaysEventsFragment);
//        TableData.TableInfo.EDITING = false;
//        Intent alarmIntent = new Intent(Version2.this, UnfinishedEventsReminderReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(Version2.this, 0, alarmIntent, 0);
//
//        Intent bootIntent = new Intent(Version2.this, ResetFinishedEventsReceiver.class);
//        pendingBootIntent = PendingIntent.getBroadcast(Version2.this, 0, bootIntent, 0);
//
//        TextView text = (TextView) findViewById(R.id.textView2);
//        Typeface font = Typeface.createFromAsset(getAssets(), "ComicSansMS.ttf");
//        text.setTypeface(font);
//        //startAlarmAtSpecificTime(12, 12, 1);
//        startAlarmManager();
//        startResetAlarmManager();
//        createStartScreen();
//        setTabHost();
        //setTabHost();
        //showBradley();
        //setColours();

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
        DataBaseOperations dbo = new DataBaseOperations(ctx);
        TextView noEvents = (TextView) viewById(R.id.todaysEventsFragmentNoEventsView);
        setToday();
        Cursor cr = dbo.getInformation(dbo, dayOfTheWeek, dateOfTheMonth);
        //LinearLayout ll = (LinearLayout) viewById(R.id.todaysEventsFragmentLinearLayout);
        expandableListView = (ListView) viewById(R.id.todaysEventsListView);
        //ll.removeAllViews();
        boolean allFinished = true;
        childList = new HashMap<>();
        eventNames = new ArrayList<>();
        eventFrequencies = new ArrayList<>();
        eventDurations = new ArrayList<>();
//        eventNames.add("+");
//        eventFrequencies.add("");
//        eventDurations.add("");
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
        eventNames.add(eventName);
        eventFrequencies.add(eventFrequency);
        eventDurations.add(eventDuration);
        for (int i = 0; i < eventNames.size(); i ++){
            List<String> deviceInfo = new ArrayList<>();
            //if(i != 0){
            deviceInfo.add(eventFrequencies.get(i));
            if(Integer.parseInt(eventDurations.get(i)) > 0){
                deviceInfo.add("Time remaining: " + eventDurations.get(i));
            }
            deviceInfo.add("Edit");
            deviceInfo.add("Finished");
            deviceInfo.add("Subtract Time");
            deviceInfo.add("Move Event To Tomorrow(Once)");
            deviceInfo.add("Move Event To Tomorrow(Indefinitely)");
            //}

            childList.put(eventNames.get(i), deviceInfo);

        }
        EventListAdapter adapter = new EventListAdapter(this, getActivity(), eventNames, eventDurations);

        expandableListView.setAdapter(adapter);
    }

    public void finishEvent(final String title){
        new AlertDialog.Builder(ctx)
                .setTitle("Event Finished")
                .setMessage("Are you sure you've finished "+ title+"?")
                .setIcon(R.drawable.ic_dialog_alert)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataBaseOperations dbo = new DataBaseOperations(ctx);
                        dbo.updateEventFinished(dbo, title);
                        setupTodaysEvents();
                        Toast.makeText(ctx, "You have finished an event!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("no", null).show();
    }

    private View viewById(int id){

        return rootView.findViewById(id);
    }

//    private void setTabHost(){
////        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
////        tabHost.setup();
////        TabSpec homeTab = tabHost.newTabSpec("First Tab");
////        TabSpec calendarTab = tabHost.newTabSpec("Second Tab");
////        TabSpec historyTab = tabHost.newTabSpec("Third Tab");
////
//////        tabHost.clearAllTabs();
////        homeTab.setIndicator("", getResources().getDrawable(R.drawable.home_icon));
////        homeTab.setContent(new Intent(this, Version2.class));
////
////        calendarTab.setIndicator("", getResources().getDrawable(R.drawable.calendar_icon));
////        calendarTab.setContent(new Intent(this, CalendarActivity.class));
////
////        historyTab.setIndicator("", getResources().getDrawable(R.drawable.history_icon));
////        historyTab.setContent(new Intent(this, PreviousHistory.class));
////
//////        ((TabHost) findViewById(R.id.tabHost)).addTab(homeTab);
//////        ((TabHost) findViewById(R.id.tabHost)).addTab(calendarTab);
//////        ((TabHost) findViewById(R.id.tabHost)).addTab(historyTab);
////        tabHost.addTab(homeTab);
////        tabHost.addTab(calendarTab);
////        tabHost.addTab(historyTab);
////        TabHost host = (TabHost)findViewById(R.id.tabHost);
////        host.setup();
////
////        //Tab 1
////        TabHost.TabSpec spec = host.newTabSpec("Tab One");
////        spec.setContent(new Intent(this, PreviousHistory.class));
////        spec.setIndicator("Tab One");
//////        host.addTab(spec);
////
////        //Tab 2
////        spec = host.newTabSpec("Tab Two");
////        spec.setContent(new Intent(this, PreviousHistory.class));
////        spec.setIndicator("Tab Two");
////  //      host.addTab(spec);
////
////        //Tab 3
////        spec = host.newTabSpec("Tab Three");
////        spec.setContent(new Intent(this, PreviousHistory.class));
////        spec.setIndicator("Tab Three");
//        //    host.addTab(spec);
//        TabLayout tabLayout = new TabLayout(this);
//        final TabLayout.Tab homeTab = tabLayout.newTab();
//        final TabLayout.Tab calendarTab = tabLayout.newTab();
//        final TabLayout.Tab historyTab = tabLayout.newTab();
//        homeTab.setIcon(R.drawable.home_icon);
//        calendarTab.setIcon(R.drawable.calendar_icon);
//        historyTab.setIcon(R.drawable.history_icon);
//        tabLayout.addTab(homeTab);
//        tabLayout.addTab(calendarTab);
//        tabLayout.addTab(historyTab);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                RelativeLayout tabLayout = (RelativeLayout) findViewById(R.id.tabLayout);
//                if(tab == homeTab){
////                    homeTab.setContent(new Intent(this, PreviousHistory.class));
//
//                }else if(tab == calendarTab){
//
//                }else{
//
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        ((RelativeLayout) findViewById(R.id.mainScreenLayout)).addView(tabLayout);
//    }
//
//    private void setExpandableListEvents(){
//        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView2);
//        //   ExpandableListView expandableListView = new ExpandableListView(this);
////        ScrollView ll = (ScrollView) findViewById(R.id.mainScrollView);
//        LinearLayout ll = (LinearLayout) findViewById(R.id.mainLinearLayout);
//        ll.addView(expandableListView);
//        HashMap<String, List<String>> childList = new HashMap<>();
//        List<String> eventNames = new ArrayList<>();
//        eventNames.add("Test Event One");
//        eventNames.add("Event Test Two");
//
//        for (int i = 0; i < eventNames.size(); i ++){
//            List<String> deviceInfo = new ArrayList<>();
//            deviceInfo.add("wednesday thursday");
//            deviceInfo.add("time remaining: 45 minutes");
//            deviceInfo.add("Edit");
//            deviceInfo.add("Finished");
//            deviceInfo.add("Subtract Time");
//            childList.put(eventNames.get(i), deviceInfo);
//        }
//        //BluetoothExpandableListAdapter adapter = new BluetoothExpandableListAdapter(this,this, eventNames, childList);
//        //expandableListView.setAdapter(adapter);
//    }
//
//    private void resetEventsDuration(DataBaseOperations dbo){
//        Cursor cr = dbo.getInformation(dbo);
//        cr.moveToFirst();
//        if(cr.moveToNext()){
//            do{
//                String eventName;
//                eventName = cr.getString(0);
//                dbo.resetDuration(dbo, eventName);
//            }while(cr.moveToNext());
//        }
//    }
//
//    public void reset(){
//        DataBaseOperations dbo = new DataBaseOperations(this);
//        dbo.resetFinished(dbo);
//        resetEventsDuration(dbo);
//        showNotification();
//        setupTodaysEvents();
//    }
//
//    public void showNotification() {
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//        Resources r = getResources();
//        Notification notification = new NotificationCompat.Builder(this)
//                .setTicker(r.getString(R.string.thursday))
//                .setSmallIcon(android.R.drawable.ic_menu_report_image)
//                .setContentTitle(r.getString(R.string.notification_title))
//                .setContentText(r.getString(R.string.notification_text))
//                .setContentIntent(pi)
//                .setSound(alarmSound)
//                .setAutoCancel(true)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notification);
//        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
//    }
//
//    public void startAlarmManager() {
//        Calendar calendar = Calendar.getInstance();
//        Calendar alarmCalender = calendar;
//        alarmCalender.set(Calendar.HOUR_OF_DAY, 19);
//        alarmCalender.set(Calendar.MINUTE, 30);
//        alarmCalender.set(Calendar.SECOND,0);
//
////        long time24h = 24*60*60*1000;
////        long timeAt09_00 = ...; // calculate from now...
////        long timeAt11_30 = ...; // calculate from now...
////
////        alarmMgr1.setInexactRepeating(AlarmManager.RTC_WAKEUP, now ,        time24h, alarmIntent);
////        alarmMgr2.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt09_00, time24h, alarmIntent);
////        alarmMgr3.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt11_30, time24h, alarmIntent);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        int interval = 1000*60*60*24;
//
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), interval, pendingIntent);
//        //Toast.makeText(this, "Alarm Set for 13:30", Toast.LENGTH_SHORT).show();
//    }
//
//    public void startResetAlarmManager(){
//        Calendar calendar = Calendar.getInstance();
//        Calendar alarmCalender = calendar;
//        alarmCalender.set(Calendar.HOUR_OF_DAY, 23);
//        alarmCalender.set(Calendar.MINUTE, 59);
//        alarmCalender.set(Calendar.SECOND,0);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        int interval = 1000 * 60 *60 * 24;
//
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), interval, pendingBootIntent);
//        //Toast.makeText(this, "Boot Alarm Set", Toast.LENGTH_SHORT).show();
//    }
//
//    public void cancelAlarmManager() {
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        manager.cancel(pendingIntent);
//        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
//    }
//
//    public void startAlarmAtSpecificTime(int hour, int minute, int repeatEveryXMinutes) {
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        int interval = 1000 * 60 * 20;
//
//        /* Set the alarm to start at time hour:minute */
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//
//        /* Repeating on every x minutes interval */
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * repeatEveryXMinutes, pendingIntent);
//    }
//
//    private void showBradley(){
//        new AlertDialog.Builder(this)
//                .setTitle("Hi Bradley")
//                .setMessage("Are you sure you want to erase all data on your phone?")
//                .setIcon(R.drawable.ic_dialog_alert)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Toast.makeText(Version2.this, "Did you like my joke?", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        new AlertDialog.Builder(Version2.this)
//                                .setTitle("You suck")
//                                .setMessage("You're not getting away that easily. Click yes.")
//                                .setIcon(R.drawable.ic_dialog_alert)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        Toast.makeText(Version2.this, "Did you like my joke?", Toast.LENGTH_LONG).show();
//                                    }
//                                })
//                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss(DialogInterface dialog) {
//                                        Toast.makeText(Version2.this, "Did you like my joke?", Toast.LENGTH_LONG).show();
//                                    }
//                                })
//                                .show();
//                    }
//                })
//                .show();
//    }
//
//    public void createStartScreen(){
//        Button calendarButton, addButton, settingsButton, historyButton;
//        Button testButton = (Button) findViewById(R.id.testButton);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //reset();
//                setExpandableListEvents();
//            }
//        });
//        calendarButton = (Button)findViewById(R.id.calendarButtonMain);
//        calendarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Version2.this, CalendarActivity.class));
//                //TableData.TableInfo.EDITING = false;
//            }
//        });
//        addButton = (Button) findViewById(R.id.addButtonMain);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Version2.this, NewEventScreen.class));
//            }
//        });
//        settingsButton = (Button) findViewById(R.id.settingsButtonMain);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Version2.this, SettingsActivity.class));
//            }
//        });
//        historyButton = (Button) findViewById(R.id.historyButtonMain);
//        historyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Version2.this, PreviousHistory.class));
//            }
//        });
//        setToday();
//        setupTodaysEvents();
//
//    }
//
//    private void setColours(){
//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mainScreenLayout);
//        rl.setBackgroundColor(Colours.backgroundColour);
//        ScrollView sv = (ScrollView) findViewById(R.id.mainScrollView);
//        sv.setBackgroundColor(Colours.foregroundColour);
//        Button calendarButton, addButton, settingsButton, historyButton;
//        Button testButton = (Button) findViewById(R.id.testButton);
//        calendarButton = (Button)findViewById(R.id.calendarButtonMain);
//        addButton = (Button) findViewById(R.id.addButtonMain);
//        settingsButton = (Button) findViewById(R.id.settingsButtonMain);
//        historyButton = (Button) findViewById(R.id.historyButtonMain);
//        testButton.setBackgroundColor(Colours.buttonColour);
//        calendarButton.setBackgroundColor(Colours.buttonColour);
//        addButton.setBackgroundColor(Colours.buttonColour);
//        settingsButton.setBackgroundColor(Colours.buttonColour);
//        historyButton.setBackgroundColor(Colours.buttonColour);
//    }
}

