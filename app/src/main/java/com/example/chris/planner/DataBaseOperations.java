package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.chris.planner.TableData.*;

import java.io.File;

/**
 * Created by Chris on 09/03/2016.
 */
public class DataBaseOperations extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public static final int database_version = 1;

    public String DELETE_QUERY = "DROP TABLE " + TableInfo.TABLE_NAME+ ";";
    public String CREATE_QUERY = "CREATE TABLE " +
                                TableInfo.TABLE_NAME +" ( " +
                                TableInfo.EVENT_NAME +" TEXT, " +
                                TableInfo.EVENT_FREQUENCY + " TEXT, " +
                                TableInfo.EVENT_DURATION + " INTEGER," +
                                TableInfo.INITIAL_EVENT_DURATION + " INTEGER," +
                                TableInfo.EVENT_FINISHED + " TEXT );" ;



    public DataBaseOperations(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, database_version);
        try{
            db = getWritableDatabase();
            this.db.execSQL(CREATE_QUERY);
        }catch(SQLiteException e){
//            db = getWritableDatabase();
//            this.db.execSQL(CREATE_QUERY);
//            Log.d("Database operations", "Table created");
        }
//        if(!doesDatabaseExist(context, TableInfo.DATABASE_NAME)){
//            this.db.execSQL(CREATE_QUERY);
//            Log.d("Database operations", "Table created");
//            putInformation(this,"","",0);
//        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DELETE_QUERY);
//        Log.d("Database operations", "Table deleted");
        //this.db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDayTable(String dayOfTheWeek, int dateOfTheMonth, String month, int year){
        String TABLE_NAME = dayOfTheWeek + ", " + month + " " + dateOfTheMonth + ", " + year;
        String CREATE_DATE_EVENT_INFO_QUERY = "CREATE TABLE " +
                TABLE_NAME +" ( " +
                TableInfo.EVENT_NAME +" TEXT, " +
                TableInfo.EVENT_FREQUENCY + " TEXT, " +
                TableInfo.EVENT_DURATION + " INTEGER," +
                TableInfo.INITIAL_EVENT_DURATION + " INTEGER," +
                TableInfo.EVENT_FINISHED + " TEXT );" ;

        db.execSQL(CREATE_DATE_EVENT_INFO_QUERY);

    }

    public Cursor getDayTableInformation(DataBaseOperations dbo, String dayOfTheWeek, int dateOfTheMonth, String month, int year){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String TABLE_NAME = dayOfTheWeek + ", " + month + " " + dateOfTheMonth + ", " + year;
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.EVENT_FINISHED};
        return sq.query(TABLE_NAME, columns, null, null, null, null, null);
    }

    public void putInformation(Activity ma, DataBaseOperations dbo, String eventName, String eventFrequency, int eventDuration){

        if(!doesEventNameAlreadyExist(dbo, eventName)){
            SQLiteDatabase sqlDB = dbo.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableInfo.EVENT_NAME, eventName);
            cv.put(TableInfo.EVENT_FREQUENCY, eventFrequency);
            cv.put(TableInfo.EVENT_DURATION, eventDuration);
            cv.put(TableInfo.INITIAL_EVENT_DURATION, eventDuration);
            cv.put(TableInfo.EVENT_FINISHED, "no");
            long k = sqlDB.insert(TableInfo.TABLE_NAME, null, cv);
            Log.d("Database operations", "One raw inserted");
        }else{
            new AlertDialog.Builder(ma)
                    .setTitle("Invalid input")
                    .setMessage(eventName + " already exists. Use a different name")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }

    }

    public Cursor getInformation(DataBaseOperations dbo){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.EVENT_FINISHED};
        //String args = "WHERE " + TableInfo.EVENT_FREQUENCY + " =? " + day + " OR " + TableInfo.EVENT_FREQUENCY + " =? " + date;
        return sq.query(TableInfo.TABLE_NAME, columns, /*args*/null, null, null, null, null);
    }

    public Cursor getInformation(DataBaseOperations dbo, String day, String date){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME, TableInfo.EVENT_FREQUENCY, TableInfo.EVENT_DURATION, TableInfo.INITIAL_EVENT_DURATION,TableInfo.EVENT_FINISHED};
        String args =TableInfo.EVENT_FREQUENCY + " LIKE '%"+day+"%' OR " + TableInfo.EVENT_FREQUENCY + " LIKE '%"+date+"%'";
        return sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);
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
                new String[] { String.valueOf(eventTitle) });
    }

    public boolean doesEventNameAlreadyExist(DataBaseOperations dbo, String eventTitle){
        SQLiteDatabase sq = dbo.getWritableDatabase();
        String[] columns = {TableInfo.EVENT_NAME};
        String args =TableInfo.EVENT_NAME + " LIKE '"+eventTitle+"' ";
        Cursor cr = sq.query(TableInfo.TABLE_NAME, columns, args, null, null, null, null);
        return cr.moveToFirst();
    }

    public void updateEventEdited(Activity ma,DataBaseOperations dbo, String eventTitle, String frequency, String duration){
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

}
