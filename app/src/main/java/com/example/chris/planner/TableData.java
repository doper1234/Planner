package com.example.chris.planner;

import android.provider.BaseColumns;

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
        public static final String EVENT_DURATION ="event_duration";
        public static final String DATABASE_NAME = "daily_events";
        public static final String TABLE_NAME = "events";
//        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
//        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
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
