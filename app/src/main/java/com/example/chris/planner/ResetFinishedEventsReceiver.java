package com.example.chris.planner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chris on 29/03/2016.
 */
public class ResetFinishedEventsReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Toast.makeText(context, "Boot completed action", Toast.LENGTH_SHORT).show();
        }
        this.context = context;
        Toast.makeText(context, "New day. Finished events reset. Database created for finished/unfinished events from yesterday.", Toast.LENGTH_SHORT).show();
        resetFinishedEvents();
    }

    public void resetFinishedEvents(){

        DataBaseOperations dbo = new DataBaseOperations(context);
        // create database to store history
        try{
            createTodayHistoryDatabase(dbo);
        }catch(Exception e){
            e.printStackTrace();
        }

        dbo.resetFinished(dbo);
        resetEventsDuration(dbo);
        //context.startActivity(new Intent(context, Version2.class));//update gui?
    }

    private void createTodayHistoryDatabase(DataBaseOperations dbo){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        dbo.createDayTable(dbo, getWeekDay(dayOfWeek), dateOfMonth, getMonth(month), year);
    }

    private String getWeekDay(int weekDay){

        String weekDayString = "";
        switch (weekDay){
            case Calendar.MONDAY: weekDayString = "monday";
                break;
            case Calendar.TUESDAY: weekDayString = "tuesday";
                break;
            case Calendar.WEDNESDAY: weekDayString = "wednesday";
                break;
            case Calendar.THURSDAY: weekDayString = "thursday";
                break;
            case Calendar.FRIDAY: weekDayString = "friday";
                break;
            case Calendar.SATURDAY: weekDayString = "saturday";
                break;
            case Calendar.SUNDAY: weekDayString = "sunday";
                break;
        }

        return weekDayString;
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
}
