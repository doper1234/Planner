package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.chris.planner.TableData.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 09/03/2016.
 */
public class DataBaseOperations extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public static final int database_version = 1;

    public String CREATE_QUERY = "CREATE TABLE " +
                                TableInfo.TABLE_NAME +" ( " +
                                TableInfo.EVENT_NAME +" TEXT, " +
                                TableInfo.EVENT_FREQUENCY + " TEXT, " +
                                TableInfo.EVENT_DURATION + " INTEGER," +
                                TableInfo.INITIAL_EVENT_DURATION + " INTEGER," +
                                TableInfo.EVENT_FINISHED + " TEXT," +
                                TableInfo.EVENT_ORDER + " INTEGER "
                                + " );" ;

    public DataBaseOperations(Context context) {

        //create the database if it doesn't exist
        super(context, TableInfo.DATABASE_NAME, null, database_version);
        try{
            db = getWritableDatabase();
            this.db.execSQL(CREATE_QUERY);
        }catch(SQLiteException e){

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDayTable(DataBaseOperations dbo, String dayOfTheWeek, int dateOfTheMonth, String month, int year){

        //create a new database for the current day at 11:59
        String TABLE_NAME = dayOfTheWeek + "_" + month + "_" + dateOfTheMonth + "_" + year;
        String CREATE_DATE_EVENT_INFO_QUERY = "CREATE TABLE " +
                TABLE_NAME +" ( " +
                TableInfo.EVENT_NAME +" TEXT, " +
                TableInfo.EVENT_FREQUENCY + " TEXT, " +
                TableInfo.EVENT_DURATION + " INTEGER," +
                TableInfo.INITIAL_EVENT_DURATION + " INTEGER," +
                TableInfo.EVENT_FINISHED + " TEXT );" ;

        db.execSQL(CREATE_DATE_EVENT_INFO_QUERY);
        //put all the information from each event from the current day into the newly created database
        Cursor yesterdayInformation = getInformation(dbo, dayOfTheWeek, Integer.toString(dateOfTheMonth));
        if(yesterdayInformation.moveToFirst()){
            do{
                String eventName, eventFrequency, eventDuration, initialEventDuration, eventFinished;
                eventName = yesterdayInformation.getString(0);
                eventFrequency = yesterdayInformation.getString(1);
                eventDuration = yesterdayInformation.getString(2);
                initialEventDuration = yesterdayInformation.getString(3);
                eventFinished = yesterdayInformation.getString(4);

                SQLiteDatabase sqlDB = dbo.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(TableInfo.EVENT_NAME, eventName);
                cv.put(TableInfo.EVENT_FREQUENCY, eventFrequency);
                cv.put(TableInfo.EVENT_DURATION, eventDuration);
                cv.put(TableInfo.INITIAL_EVENT_DURATION, initialEventDuration);
                cv.put(TableInfo.EVENT_FINISHED, eventFinished);
                sqlDB.insert(TABLE_NAME, null, cv);
                Log.d("Database operations", "New previous day database created.");
            }while (yesterdayInformation.moveToNext());
        }
    }

    public Cursor getDayTableInformation(DataBaseOperations dbo, String dayOfTheWeek, int dateOfTheMonth, String month, int year){
        //return data from a specific day
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String TABLE_NAME = dayOfTheWeek + "_" + month + "_" + dateOfTheMonth + "_" + year;
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.EVENT_FINISHED};
        return sq.query(TABLE_NAME, columns, null, null, null, null, null);
    }

    public void updateOrder(DataBaseOperations dbo, String title, boolean moveUp){
        // determine which way we'll be moving
        int upOrDown;
        if(moveUp){
            upOrDown = 1;
        }else{
            upOrDown = -1;
        }

        //get the position of the event that will be moved
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_ORDER};
        String args =TableInfo.EVENT_NAME + " LIKE '%"+title+"%'";
        Cursor eventOrderCursor = sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);

        int currentEventPosition;
        if(eventOrderCursor.moveToFirst()){

            //if the order is greater than zero, calculate the new position
            currentEventPosition = eventOrderCursor.getInt(0);
            Log.d("Here", currentEventPosition + " pos");
            if(currentEventPosition <= 0){
                return;
            }else{
                currentEventPosition -= upOrDown;
            }

            //then update every other event

            //first the events that have a greater than the one we are updating
            String[] columns2 = {TableInfo.EVENT_NAME, TableInfo.EVENT_ORDER};
            String args2 =TableInfo.EVENT_ORDER+" >= "+ currentEventPosition +"";
            Cursor eventsGreaterToChange = sq.query(TableInfo.TABLE_NAME, columns2, args2, null, null, null, null);

            if(eventsGreaterToChange.moveToFirst()){
                do{
                    updateEventOrder(eventsGreaterToChange.getString(0), eventsGreaterToChange.getInt(1), false, sq);
                }while(eventsGreaterToChange.moveToNext());
            }

            //then the events that have are less than the one we are updating
            String[] columns3 = {TableInfo.EVENT_NAME, TableInfo.EVENT_ORDER};
            String args3 =TableInfo.EVENT_ORDER+" < "+ currentEventPosition +"";
            Cursor eventsLessToChange = sq.query(TableInfo.TABLE_NAME, columns3, args3, null, null, null, null);

            if(eventsLessToChange.moveToFirst()){
                do {
                    updateEventOrder(eventsLessToChange.getString(0), eventsLessToChange.getInt(1), true, sq);
                }while(eventsLessToChange.moveToNext());
            }

            //and finally, assign the new position to the event
            ContentValues values = new ContentValues();
            values.put(TableInfo.EVENT_ORDER, currentEventPosition);
            sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                    new String[]{String.valueOf(title)});


        }
    }

    public void updateEventOrder(String title, int currentPosition, boolean moveUp, SQLiteDatabase sq){
        Log.d("Current pos", ""+currentPosition);
        int upOrDown, newPosition;
        if(moveUp){
            upOrDown = 1;
        }else{
            upOrDown = -1;
        }
        newPosition = currentPosition + upOrDown;

        ContentValues values = new ContentValues();
        values.put(TableInfo.EVENT_ORDER, newPosition);
        sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                new String[]{String.valueOf(title)});
    }

    public void putInformation(DataBaseOperations dbo, String eventName, String eventFrequency, int eventDuration){

        if(!doesEventNameAlreadyExist(dbo, eventName)){
            SQLiteDatabase sqlDB = dbo.getWritableDatabase();
            long numRows = DatabaseUtils.queryNumEntries(db, TableInfo.TABLE_NAME);
            ContentValues cv = new ContentValues();
            cv.put(TableInfo.EVENT_NAME, eventName);
            cv.put(TableInfo.EVENT_FREQUENCY, eventFrequency);
            cv.put(TableInfo.EVENT_DURATION, eventDuration);
            cv.put(TableInfo.INITIAL_EVENT_DURATION, eventDuration);
            cv.put(TableInfo.EVENT_FINISHED, "no");
            cv.put(TableInfo.EVENT_ORDER, numRows);
            long k = sqlDB.insert(TableInfo.TABLE_NAME, null, cv);
            Log.d("Database operations", "One raw inserted");
        }else{
//            new AlertDialog.Builder(ma)
//                    .setTitle("Invalid input")
//                    .setMessage(eventName + " already exists. Use a different name")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton(android.R.string.yes, null)
//                    .show();
        }

    }

    public Cursor getAllInformation(DataBaseOperations dbo){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.INITIAL_EVENT_DURATION, TableInfo.EVENT_FINISHED, TableInfo.EVENT_ORDER};
        return sq.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);
    }

    public Cursor getInformation(DataBaseOperations dbo){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.EVENT_FINISHED};
        //String args = "WHERE " + TableInfo.EVENT_FREQUENCY + " =? " + day + " OR " + TableInfo.EVENT_FREQUENCY + " =? " + date;
        return sq.query(TableInfo.TABLE_NAME, columns, /*args*/null, null, null, null, null);
    }

    public Cursor getInformation(DataBaseOperations dbo, String title){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.INITIAL_EVENT_DURATION,TableInfo.EVENT_FINISHED};
        String args =TableInfo.EVENT_NAME + " LIKE '%"+title+"%'";
        return sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);
    }

    public Cursor getInformation(DataBaseOperations dbo, String day, String date){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.INITIAL_EVENT_DURATION,TableInfo.EVENT_FINISHED};
        String args =TableInfo.EVENT_FREQUENCY + " LIKE '%"+day+"%' OR " + TableInfo.EVENT_FREQUENCY + " LIKE '%"+date+"%'";
        return sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null,TableInfo.EVENT_ORDER +" ASC");

    }

    public void updateTime(DataBaseOperations dbo, String eventTitle, String duration){

        SQLiteDatabase sq = dbo.getWritableDatabase();
        //String whereCdn = TableInfo.EVENT_NAME + " = " + eventTitle ;
        ContentValues values = new ContentValues();
        values.put(TableInfo.EVENT_DURATION, duration);
        //values.put(TableInfo.INITIAL_EVENT_DURATION, duration);
        // updating row
        sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                new String[] { String.valueOf(eventTitle) });

    }

    public void updateEventFinished(DataBaseOperations dbo, String eventTitle){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        //String whereCdn = TableInfo.EVENT_NAME + " = " + eventTitle ;
        ContentValues values = new ContentValues();
        values.put(TableInfo.EVENT_FINISHED, "yes");

        // updating row
        sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                new String[]{String.valueOf(eventTitle)});
    }

    public boolean doesEventNameAlreadyExist(DataBaseOperations dbo, String eventTitle){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME};
        String args =TableInfo.EVENT_NAME + " LIKE '"+eventTitle+"' ";
        Cursor cr = sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);
        return cr.moveToFirst();
    }

    public void updateEventEdited(DataBaseOperations dbo, String eventTitle, String frequency, String duration){
        //if(!doesEventNameAlreadyExist(dbo, eventTitle)){
            SQLiteDatabase sq = dbo.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TableInfo.EVENT_NAME, eventTitle);
            values.put(TableInfo.EVENT_FREQUENCY, frequency);
            values.put(TableInfo.EVENT_DURATION, duration);
            values.put(TableInfo.INITIAL_EVENT_DURATION, duration);

            // updating row
            sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                    new String[]{String.valueOf(eventTitle)});
//        }else{
//            new AlertDialog.Builder(ma)
//                    .setTitle("Invalid input")
//                    .setMessage(eventTitle + " already exists. Use a different name")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton(android.R.string.yes, null)
//                    .show();
//        }

    }

    public void deleteEvent(DataBaseOperations dbo, String eventTitle, String eventFrequency, String duration){
        SQLiteDatabase sq = dbo.getWritableDatabase();

        //String[] whereArgs = new String[] { coin };
//        ourDatabase.delete(DATABASE_TABLE, KEYNAME + "=?", whereArgs);
//        String selection = "DELETE FROM + " + TableInfo.EVENT_NAME + " WHERE " + TableInfo.EVENT_NAME + ;
        //String columns[] = {TableInfo.EVENT_NAME};
        String args[] = {eventTitle};
        sq.delete(TableInfo.TABLE_NAME, TableInfo.EVENT_NAME + "=?", args);

    }

    public void deleteEvent(DataBaseOperations dbo, String eventTitle){
        SQLiteDatabase sq = dbo.getWritableDatabase();

        //String[] whereArgs = new String[] { coin };
//        ourDatabase.delete(DATABASE_TABLE, KEYNAME + "=?", whereArgs);
//        String selection = "DELETE FROM + " + TableInfo.EVENT_NAME + " WHERE " + TableInfo.EVENT_NAME + ;
        //String columns[] = {TableInfo.EVENT_NAME};
        String args[] = {eventTitle};
        sq.delete(TableInfo.TABLE_NAME, TableInfo.EVENT_NAME + "=?", args);

    }

    public void resetFinished(DataBaseOperations dbo){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        //String whereCdn = TableInfo.EVENT_NAME + " = " + eventTitle ;
        ContentValues values = new ContentValues();
        values.put(TableInfo.EVENT_FINISHED, "no");

        // updating row
        sq.update(TableInfo.TABLE_NAME, values, null, null);
    }

    public void resetDuration(DataBaseOperations dbo, String eventTitle){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String initialDuration = "";
        String[] columns = {TableInfo.INITIAL_EVENT_DURATION};
        String args =TableInfo.EVENT_NAME + " LIKE '%"+eventTitle+"%' ";
        Cursor cr = sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);
        while (cr.moveToNext()){
            initialDuration = cr.getString(0);
        }

        Log.d("Reset Duration",initialDuration);

        ContentValues values = new ContentValues();
        values.put(TableInfo.EVENT_DURATION, initialDuration);

        // updating row
        sq.update(TableInfo.TABLE_NAME, values, TableInfo.EVENT_NAME + " LIKE ?",
                new String[]{String.valueOf(eventTitle)});

    }

    public void deleteDatabase(){
        db.delete(TableInfo.TABLE_NAME, null, null);
    }

    public List<String> syncEvents(List<String> eventsToSyncWith){

        return new ArrayList<>();
    }

    public void close(){
        db.close();
    }

}
