package com.example.chris.planner;

import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Chris on 09/03/2016.
 */
public final class TableData {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TableData() {}

    /* Inner class that defines the table contents */
    public static abstract class TableInfo implements BaseColumns {

        public static final String EVENT_NAME = "event_name";
        public static final String EVENT_FREQUENCY = "event_frequency";
        public static final String INITIAL_EVENT_DURATION = "initial_event_duration";
        public static final String EVENT_DURATION ="event_duration";
        public static final String EVENT_FINISHED ="event_finished";
        public static final String DATABASE_NAME = "daily_events";
        public static final String TABLE_NAME = "events";
        public static boolean EDITING;
        public static boolean DISPLAY_ONLY;
        public static String INCOMING_TABLE = "";
        public static boolean READY_TO_LOAD = false;

//        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
//        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static String dayOfTheWeek(){
        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        String dayOfTheWeek = getDateInfo[0];

        return dayOfTheWeek;
    }

    public static String yesterdayOfTheWeek(){
        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        String dayOfTheWeek = getDateInfo[0];

        return getYesterday(dayOfTheWeek);
    }

    public static String dateOfTheMonth(){
        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        String splitDate[] = getDateInfo[1].split(" ");
        String dateOfTheMonth = splitDate[2];

        return dateOfTheMonth;
    }

    public static String yesterdayDateOfTheMonth(){
        DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        Calendar calobj = Calendar.getInstance();
        String[] getDateInfo;
        getDateInfo = df.format(calobj.getTime()).toString().split(",");
        String splitDate[] = getDateInfo[1].split(" ");
        int dateOfTheMonth = Integer.parseInt(splitDate[2]);
        dateOfTheMonth -=1;

        String result = Integer.toString(dateOfTheMonth);

        return result;
    }

    private static  String getYesterday(String day){
        String result = "";

        if(day.equalsIgnoreCase("sunday")){
            result = "saturday";
        }else if(day.equalsIgnoreCase("monday")){
            result = "sunday";
        }else if(day.equalsIgnoreCase("tuesday")){
            result = "monday";
        }else if(day.equalsIgnoreCase("wednesday")){
            result = "tuesday";
        }else if(day.equalsIgnoreCase("thursday")){
            result = "wednesday";
        }else if(day.equalsIgnoreCase("friday")){
            result = "thursday";
        }else if(day.equalsIgnoreCase("saturday")){
            result = "friday";
        }
        return result;
    }
//
//    private static final String TEXT_TYPE = " TEXT";
//    private static final String COMMA_SEP = ",";
//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + TableInfo.TABLE_NAME + " (" +
//                    TableInfo._ID + " INTEGER PRIMARY KEY," +
//                    TableInfo.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
//                    TableInfo.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
//     // Any other options for the CREATE command
//            " )";
//
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + TableInfo.TABLE_NAME;
}
