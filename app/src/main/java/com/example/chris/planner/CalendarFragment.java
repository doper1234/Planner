package com.example.chris.planner;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Chris on 11/04/2016.
 */
public class CalendarFragment extends Fragment {

    private Context ctx;
    private SlideScreenActivity slideScreenActivity;
    private ViewGroup rootView;

    public CalendarFragment(Context context, SlideScreenActivity ssa){
        ctx = context;
        slideScreenActivity = ssa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.calendar_activity, container, false);
        createCalendarScreen();

        return rootView;
//
    }

    private View findViewById(int id){
        return rootView.findViewById(id);
    }

    public void createCalendarScreen(){
        final CalendarView calendarView = (CalendarView) findViewById(R.id.todayCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //TextView calendarTextView = (TextView) findViewById(R.id.calendarTextView);
                Calendar current = Calendar.getInstance();
                Calendar selected = Calendar.getInstance();
                selected.setTimeInMillis(view.getDate());
                int dayOfWeek = selected.get(Calendar.DAY_OF_WEEK);
                if (current.getTimeInMillis() > selected.getTimeInMillis()) {
                    getPreviousDayEvents(dayOfWeek, dayOfMonth, month, year);
                } else {
                    getAndDisplayEvents(dayOfWeek, dayOfMonth);
                }

                //calendarTextView.setText(year + " " + month + " " + dayOfMonth + " " + dayOfWeek);
            }
        });

        Button todayButton = (Button) findViewById(R.id.backToTodayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selected = Calendar.getInstance();
                calendarView.setDate(selected.getTimeInMillis());
            }
        });

        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        String dayOfTheWeek = getDateInfo[0];
        int weekDay = getWeekDay(dayOfTheWeek);
        String splitDate[] = getDateInfo[1].split(" ");
        String dateOfTheMonth = splitDate[2];
        getAndDisplayEvents(weekDay, Integer.parseInt(dateOfTheMonth));
    }

    private void noEvents(){
//        LinearLayout ll = (LinearLayout) findViewById(R.id.scrollLinearLayout);
//        ll.removeAllViews();
//        TextView display = (TextView) findViewById(R.id.plannedTextView);
//        //display.setText(R.string.planned_on_this_yesterday);
//        TextView t = new TextView(ctx);
//        t.setText("You had no events on this day");
//        ll.addView(t);
    }

    private void getPreviousDayEvents(int dayOfWeek, int dayOfMonth, int month, int year){
        List<String> eventTitles = new ArrayList<>();
        try{
//            //LinearLayout ll = (LinearLayout) findViewById(R.id.scrollLinearLayout);
//            ll.removeAllViews();
//            TextView display = (TextView) findViewById(R.id.plannedTextView);
//            //display.setText(R.string.planned_on_this_yesterday);
//            TextView t = new TextView(ctx);
//            t.setText("Week day: " + dayOfWeek + " date: " + dayOfMonth + " month: " + month + " year:" + year);
//            ll.addView(t);
            DataBaseOperations dbo = new DataBaseOperations(ctx);
            Cursor cr = dbo.getDayTableInformation(dbo, getWeekDayString(dayOfWeek), dayOfMonth, getMonth(month), year);
            if(cr.moveToFirst()){
                do{
                    String eventName, eventFinished;
                    int eventDuration;
                    eventName = cr.getString(0);
                    eventDuration = Integer.parseInt(cr.getString(2));
                    eventFinished = cr.getString(3);
                    TextView eventInfoTextView = new TextView(ctx);
                    if(eventFinished.equalsIgnoreCase("yes")){
                        eventTitles.add(eventName + " was finished!");
                    }else if(!eventFinished.equalsIgnoreCase("yes")  && eventDuration > 0){
                        eventInfoTextView.setText(eventName + " wasn't finished. There were " + eventDuration + " minutes left.");
                        if(eventDuration == 1){
                            eventTitles.add(eventName + " wasn't finished. There was one minute left.");
                        }
                    }else{
                        eventTitles.add(eventName + " wasn't finished.");
                    }
                }while(cr.moveToNext());
            }
        }catch(Exception e){
            //e.printStackTrace();
            eventTitles.add("No events recorded for this day");
            noEvents();
        }
        ListView listView = (ListView) findViewById(R.id.calendarListView);
        listView.setAdapter(new ArrayAdapter<>(ctx, R.layout.event_child_layout, R.id.eventTitleTextView, eventTitles));
    }

    private void getAndDisplayEvents(int dayOfWeek, int dayOfMonth){
        DataBaseOperations dbo = new DataBaseOperations(ctx);
        //TextView display = (TextView) findViewById(R.id.plannedTextView);
//        display.setText(R.string.planned_on_this_day);
        //LinearLayout ll = (LinearLayout) findViewById(R.id.scrollLinearLayout);
       // ll.removeAllViews();
        List<String> eventTitles = new ArrayList<>();
        String weekDay = getWeekDayString(dayOfWeek);
        Cursor cr = dbo.getInformation(dbo, weekDay, Integer.toString(dayOfMonth));
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished;
                int eventDuration;

                // eventFinished = cr.getString(4);
                // if(eventFinished.equalsIgnoreCase("no")){
                eventName = cr.getString(0);
                eventFrequency = cr.getString(1);
                eventDuration = Integer.parseInt(cr.getString(2));
                eventTitles.add(eventName);
                //new Event(ctx, eventName, eventFrequency, eventDuration, ll);

                //}

            }while(cr.moveToNext());
        }else{
            eventTitles.add("You won't have anything to do on this day.");
        }
        ListView listView = (ListView) findViewById(R.id.calendarListView);
        listView.setAdapter(new ArrayAdapter<>(ctx, R.layout.event_child_layout, R.id.eventTitleTextView, eventTitles));

    }

    private int getWeekDay(String day){
        int result = 0;

        if(day.equalsIgnoreCase("sunday")){
            result = 1;
        }else if(day.equalsIgnoreCase("monday")){
            result = 2;
        }else if(day.equalsIgnoreCase("tuesday")){
            result = 3;
        }else if(day.equalsIgnoreCase("wednesday")){
            result = 4;
        }else if(day.equalsIgnoreCase("thursday")){
            result = 5;
        }else if(day.equalsIgnoreCase("friday")){
            result = 6;
        }else if(day.equalsIgnoreCase("saturday")){
            result = 7;
        }
        return result;
    }

    private String getWeekDayString(int day){
        String weekDay = "";
        switch (day){
            case 1: weekDay = "sunday";
                break;
            case 2: weekDay = "monday";
                break;
            case 3: weekDay = "tuesday";
                break;
            case 4: weekDay = "wednesday";
                break;
            case 5: weekDay = "thursday";
                break;
            case 6: weekDay = "friday";
                break;
            case 7: weekDay = "saturday";
                break;
        }

        return weekDay;
    }

    private String getMonth(int month){

        String monthResult = "";
        switch(month){
            case Calendar.JANUARY: monthResult = "january";
                break;
            case Calendar.FEBRUARY: monthResult = "february";
                break;
            case Calendar.MARCH: monthResult = "march";
                break;
            case Calendar.APRIL: monthResult = "april";
                break;
            case Calendar.MAY: monthResult = "may";
                break;
            case Calendar.JUNE: monthResult = "june";
                break;
            case Calendar.JULY: monthResult = "july";
                break;
            case Calendar.AUGUST: monthResult = "august";
                break;
            case Calendar.SEPTEMBER: monthResult = "september";
                break;
            case Calendar.OCTOBER: monthResult = "october";
                break;
            case Calendar.NOVEMBER: monthResult = "november";
                break;
            case Calendar.DECEMBER: monthResult = "december";
                break;
        }
        return monthResult;
    }
}
