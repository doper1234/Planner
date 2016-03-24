package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

        String dayOfTheWeek = "";
        String dateOfTheMonth = "";
        Context ctx = this;
        SeekBar timeSeekBar;
        Button add, addToPlanner, cancel, back;
        ScrollView mainScrollView;
        TextView dateView;
        RelativeLayout rl;
        RelativeLayout.LayoutParams lp;
        LinearLayout ll;
        LinearLayout.LayoutParams llp;
        String file_name = "stored_events";
        boolean editScreen;

//        public MainActivity(boolean editScreen){
//                this.editScreen = editScreen;
//        }
//
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //new ResetScheduleTimer().start(this);
                newDay();
                rl = (RelativeLayout)findViewById(R.id.main);
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ll = (LinearLayout)findViewById(R.id.linearLayout);
                llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                //dbo.putInformation(dbo,"Make vegetables", "Thursdays", 1);
                //Toast.makeText(getBaseContext(),"Event saved!", Toast.LENGTH_LONG).show();
                mainScreen();

//

        }


        public void newEventScreen(){
                setContentView(R.layout.new_event);
                setupCheckBoxListeners();
                timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
                timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                TextView timeView = (TextView) findViewById(R.id.timeView);
                                int minutes = seekBar.getProgress();
                                int hours = minutes /60;
                                if(minutes == 1){
                                        timeView.setText(minutes + " minute");
                                }else if (minutes >=60){
                                        timeView.setText(hours + " hour " + minutes % 60 + " minutes");
                                }else{
                                        timeView.setText(minutes + " minutes");
                                }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                });
                addToPlanner = (Button) findViewById(R.id.addToEventsButton);
                addToPlanner.setOnClickListener(new View.OnClickListener() {
                        EditText titleView = (EditText)findViewById(R.id.eventTitle);
                        String title = titleView.getText().toString();
                        @Override
                        public void onClick(View v) {
                                new AlertDialog.Builder(ctx)
                                        .setTitle("Add new event?")
                                        .setMessage("Add "+ title+" to events?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                        final int duration = timeSeekBar.getProgress();
                                                        if (!getFrequency().equalsIgnoreCase("")) {
                                                                addNewItem(getFrequency(), duration, false);
                                                                mainScreen();
                                                                Toast.makeText(ctx, "Event added!", Toast.LENGTH_LONG).show();
                                                        }

                                                }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();

                      }
                });
                cancel = (Button) findViewById(R.id.cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mainScreen();
                        }
                });
        }

        public void editEventScreen(final String title, final String frequency, final int duration){
                setContentView(R.layout.new_event);
                setupCheckBoxListeners();
                Button deleteButton = (Button) findViewById(R.id.deleteButton);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                new AlertDialog.Builder(ctx)
                                        .setTitle("Delete event?")
                                        .setMessage("Delete "+ title + "?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                        deleteEvent(title, frequency, duration);
                                                        mainScreen();
                                                        Toast.makeText(ctx, "Event deleted!", Toast.LENGTH_LONG).show();
                                                }})
                                        .setNegativeButton(android.R.string.no, null).show();

                        }
                });
                EditText eventTitle = (EditText) findViewById(R.id.eventTitle);
                eventTitle.setText(title);
                setCheckBoxes(frequency);
                timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
                timeSeekBar.setProgress(duration);
                setDuration();
                timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                TextView timeView = (TextView) findViewById(R.id.timeView);
                                int minutes = seekBar.getProgress();
                                int hours = minutes / 60;
                                if (minutes == 1) {
                                        timeView.setText(minutes + " minute");
                                } else if (minutes >= 60) {
                                        timeView.setText(hours + " hour " + minutes % 60 + " minutes");
                                } else {
                                        timeView.setText(minutes + " minutes");
                                }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                });
                addToPlanner = (Button) findViewById(R.id.addToEventsButton);
                addToPlanner.setText("save");
                addToPlanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                new AlertDialog.Builder(ctx)
                                        .setTitle("Save event?")
                                        .setMessage("Save "+ title+"?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                        final int duration = timeSeekBar.getProgress();
                                                        if (!getFrequency().equalsIgnoreCase("")) {
                                                                addNewItem(getFrequency(), duration, true);
                                                                Toast.makeText(ctx, "yes", Toast.LENGTH_LONG);
                                                        } else {
                                                                Toast.makeText(ctx,"nope", Toast.LENGTH_LONG);
                                                                new AlertDialog.Builder(ctx)
                                                                        .setTitle("Invalid input")
                                                                        .setMessage("You must select a day or date first")
                                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                                        .setPositiveButton(android.R.string.yes, null);
                                                        }

                                                }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();
//                                                }})
//                                        .setNegativeButton(android.R.string.no, null).show();10


                        }
                });
                cancel = (Button) findViewById(R.id.cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mainScreen();
                        }
                });
        }

        private void setDuration(){
                TextView timeView = (TextView) findViewById(R.id.timeView);
                timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
                int minutes = timeSeekBar.getProgress();
                int hours = minutes /60;
                if(minutes == 1){
                        timeView.setText(minutes + " minute");
                }else if (minutes >=60){
                        timeView.setText(hours + " hour " + minutes % 60 + " minutes");
                }else{
                        timeView.setText(minutes + " " + getResources().getText(R.string.minutes));
                }
        }

        private void setCheckBoxes(String frequency){
                CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
                if(frequency.contains("monday")){
                       mondayCheckBox = (CheckBox) findViewById(R.id.mondayCheckBox);
                        mondayCheckBox.setChecked(true);
               }
               if(frequency.contains("tuesday")){
                       tuesdayCheckBox = (CheckBox) findViewById(R.id.tuesdayCheckBox);
                       tuesdayCheckBox.setChecked(true);
               }
                if(frequency.contains("wednesday")){
                        wednesdayCheckBox = (CheckBox) findViewById(R.id.wednesdayCheckBox);
                        wednesdayCheckBox.setChecked(true);
                }
                if(frequency.contains("thursday")){
                        thursdayCheckBox = (CheckBox) findViewById(R.id.thursdayCheckBox);
                        thursdayCheckBox.setChecked(true);
                }
                if(frequency.contains("friday")){
                        fridayCheckBox = (CheckBox) findViewById(R.id.fridayCheckBox);
                        fridayCheckBox.setChecked(true);
                }
                if(frequency.contains("saturday")){
                        saturdayCheckBox = (CheckBox) findViewById(R.id.saturdayCheckBox);
                        saturdayCheckBox.setChecked(true);
                }
                if(frequency.contains("sunday")){
                        sundayCheckBox = (CheckBox) findViewById(R.id.sundayCheckBox);
                        sundayCheckBox.setChecked(true);
                }
                try{
                        Integer.parseInt(frequency);
                        onceAMonthCheckbox = (CheckBox) findViewById(R.id.onceAMonthCheckBox);
                        onceAMonthCheckbox.setChecked(true);
                        EditText specificDateEditText = (EditText) findViewById(R.id. specificDateEditText);
                        specificDateEditText.setText(frequency);
                }catch(NumberFormatException e){

                }

        }

        public void mainScreen(){
                String getDateInfo[];
                setContentView(R.layout.activity_main);
                DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
                Calendar calobj = Calendar.getInstance();
                dateView = (TextView) findViewById(R.id.dateView);
                dateView.setText(df.format(calobj.getTime()));
                Button testButton = (Button) findViewById(R.id.testButton);
                testButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                reset();
                        }
                });
                back = (Button) findViewById(R.id.backButton);
                back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, StartScreen.class));
                                TableData.TableInfo.EDITING = false;
                                //new MainActivity(false);
                                //Toast.makeText(MainActivity.this, "main screen", Toast.LENGTH_LONG).show();
                        }
                });
                add = (Button) findViewById(R.id.addButton);
                add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                newEventScreen();
                        }
                });

                getDateInfo = dateView.getText().toString().split(",");
                dayOfTheWeek = getDateInfo[0];
                String splitDate[] = getDateInfo[1].split(" ");
                dateOfTheMonth = splitDate[2];

                loadEvents();
                //Toast.makeText(this, "Today is " + dayOfTheWeek + " " + dateOfTheMonth, Toast.LENGTH_LONG).show();
                Log.d("Day of the week", dayOfTheWeek);
                Log.d("Date of the month", dateOfTheMonth);

        }

        private void addNewItem(String frequency, int duration, boolean editing){
                EditText titleView = (EditText)findViewById(R.id.eventTitle);
                String title = titleView.getText().toString();
                DataBaseOperations dbo = new DataBaseOperations(ctx);

                if(editing){
                        dbo.updateEventEdited(this, dbo, title, frequency, Integer.toString(duration));
                        mainScreen();
                        Toast.makeText(ctx, "Event saved!", Toast.LENGTH_LONG).show();
                }else{
                        if(!dbo.doesEventNameAlreadyExist(dbo, title)){
                                dbo.putInformation(this, dbo, title, frequency, duration);
                                mainScreen();
                                Toast.makeText(ctx, "Event saved!", Toast.LENGTH_LONG).show();
                        }
                }
        }

//        private void updateItem(String frequency, int duration){
//                EditText titleView = (EditText)findViewById(R.id.eventTitle);
//                String title = titleView.getText().toString();
//                DataBaseOperations dbo = new DataBaseOperations(ctx);
//                dbo.updateEventEdited(dbo, title, frequency, Integer.toString(duration));
//        }

        private void loadEvents() {
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                TextView noEvents = (TextView) findViewById(R.id.noEventsView);
                if(TableData.TableInfo.EDITING){
                        Cursor cr = dbo.getInformation(dbo);

                        if(cr.moveToFirst()){
                                do{
                                        String eventName, eventFrequency;
                                        int eventDuration;
                                        eventName = cr.getString(0);
                                        eventFrequency = cr.getString(1);
                                        eventDuration = Integer.parseInt(cr.getString(2));
                                        new Event(this, eventName, eventFrequency, eventDuration, ll);
                                }while(cr.moveToNext());
                                noEvents.setVisibility(View.INVISIBLE);
                        }else{
                                noEvents.setVisibility(View.VISIBLE);
                                noEvents.setText("You have no events. Click add to add some!");
                        }
                }else{
                        Cursor cr = dbo.getInformation(dbo, dayOfTheWeek, dateOfTheMonth);
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


//                try {   String message;
//                        FileInputStream fileInputStream = openFileInput(file_name);
//                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//                        BufferedReader reader = new BufferedReader(inputStreamReader);
//                        StringBuffer stringBuffer = new StringBuffer();
//                        while((message = reader.readLine()) != null){
//
//                                stringBuffer.append(message + "/n");
//                                //for (int i = 0; i < items.length; i++) {
//                                Event e = new Event(this, message, "new Item?");
//                                //}
//                        }
//                }catch(FileNotFoundException e){
//                        e.printStackTrace();
//                }catch(IOException e){
//
//                }
                //Event e = new Event(this, "Do the dishes","Everyday", 0);
                //Event e1 = new Event(this, "Go for a walk","Every thursday", 4);
//                Event e3 = new Event(this, "test 1", "56");
//                try {
//                        InputStream instream = getResources().openRawResource(getResources().getIdentifier("raw/events.txt", "raw", getPackageName()));
//                        if (instream != null) {
//                                InputStreamReader inputreader = new InputStreamReader(instream);
//                                Log.e("out", "print?");
//                                BufferedReader reader = new BufferedReader(inputreader);
//                                String[] items = reader.readLine().split(" ");
//                                for (int i = 0; i < items.length; i++) {
//                                        Event e = new Event(this, items[i], "hi guys");
//                                }
//                        }
//                } catch (Exception ex){
//
//                        Log.e("out", "print?not");
//
//                }
//                try{
//                        InputStream iS = getResources().getAssets().open("events.txt");
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
//                        String[] items = reader.readLine().split(" ");
//                        for (int i = 0; i < items.length; i++) {
//                                Event e = new Event(this, items[i], "new Item?");
//                        }
//                }catch(Exception e){
//
//                }

//                try {
//                        InputStream inputStream = openFileInput("events.txt");
//                        if ( inputStream != null ) {
//                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                                String receiveString = "";
//
//                                String[] items = bufferedReader.readLine().split(" ");
//                                for (int i = 0; i < items.length; i++) {
//                                        Event e = new Event(this, items[i], "hi guys");
//                                }
//                                Event e = new Event(this, "Sphagetti", "all day");
//                                Event e1 = new Event(this, "Dishes", "all day");
//                                Event e2 = new Event(this, "Practice guitar", "2 hours");
//                                Event e4 = new Event(this, "Go to work", "all day");
//                        }
//                }
//                catch (FileNotFoundException e) {
//                        Log.e("login activity", "File not found: " + e.toString());
//                } catch (IOException e) {
//                        Log.e("login activity", "Can not read file: " + e.toString());
//                }
        }

        private void setupCheckBoxListeners(){
                final CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
                mondayCheckBox = (CheckBox) findViewById(R.id.mondayCheckBox);
                tuesdayCheckBox = (CheckBox) findViewById(R.id.tuesdayCheckBox);
                wednesdayCheckBox = (CheckBox) findViewById(R.id.wednesdayCheckBox);
                thursdayCheckBox = (CheckBox) findViewById(R.id.thursdayCheckBox);
                fridayCheckBox = (CheckBox) findViewById(R.id.fridayCheckBox);
                saturdayCheckBox = (CheckBox) findViewById(R.id.saturdayCheckBox);
                sundayCheckBox = (CheckBox) findViewById(R.id.sundayCheckBox);
                onceAMonthCheckbox = (CheckBox) findViewById(R.id.onceAMonthCheckBox);
                EditText specificDateEditText = (EditText) findViewById(R.id.specificDateEditText);
                mondayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(mondayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                tuesdayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(tuesdayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                wednesdayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(wednesdayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                thursdayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(thursdayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                fridayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(fridayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                saturdayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(saturdayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                sundayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(sundayCheckBox.isChecked()){
                                        if(onceAMonthCheckbox.isChecked()){
                                                onceAMonthCheckbox.setChecked(false);
                                        }
                                }
                        }
                });
                onceAMonthCheckbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(onceAMonthCheckbox.isChecked()){
                                        if(mondayCheckBox.isChecked()){
                                                mondayCheckBox.setChecked(false);
                                        }
                                        if(tuesdayCheckBox.isChecked()){
                                                tuesdayCheckBox.setChecked(false);
                                        }
                                        if(wednesdayCheckBox.isChecked()){
                                                wednesdayCheckBox.setChecked(false);
                                        }
                                        if(thursdayCheckBox.isChecked()){
                                                thursdayCheckBox.setChecked(false);
                                        }
                                        if(fridayCheckBox.isChecked()){
                                                fridayCheckBox.setChecked(false);
                                        }
                                        if(saturdayCheckBox.isChecked()){
                                                saturdayCheckBox.setChecked(false);
                                        }
                                        if(sundayCheckBox.isChecked()){
                                                sundayCheckBox.setChecked(false);
                                        }
                                }
                        }
                });

        }

        private String getFrequency(){
                String frequency = "";
                CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
                mondayCheckBox = (CheckBox) findViewById(R.id.mondayCheckBox);
                tuesdayCheckBox = (CheckBox) findViewById(R.id.tuesdayCheckBox);
                wednesdayCheckBox = (CheckBox) findViewById(R.id.wednesdayCheckBox);
                thursdayCheckBox = (CheckBox) findViewById(R.id.thursdayCheckBox);
                fridayCheckBox = (CheckBox) findViewById(R.id.fridayCheckBox);
                saturdayCheckBox = (CheckBox) findViewById(R.id.saturdayCheckBox);
                sundayCheckBox = (CheckBox) findViewById(R.id.sundayCheckBox);
                onceAMonthCheckbox = (CheckBox) findViewById(R.id.onceAMonthCheckBox);
                EditText specificDateEditText = (EditText) findViewById(R.id.specificDateEditText);
                mondayCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                });
                if(mondayCheckBox.isChecked()){
                        frequency = frequency + "monday ";
                }
                if(tuesdayCheckBox.isChecked()){
                        frequency = frequency + "tuesday ";
                }
                if(wednesdayCheckBox.isChecked()){
                        frequency = frequency + "wednesday ";
                }
                if(thursdayCheckBox.isChecked()){
                        frequency = frequency + "thursday ";
                }
                if(fridayCheckBox.isChecked()){
                        frequency = frequency + "friday ";
                }
                if(saturdayCheckBox.isChecked()){
                        frequency = frequency + "saturday ";
                }
                if(sundayCheckBox.isChecked()){
                        frequency = frequency + "sunday ";
                }
                if(onceAMonthCheckbox.isChecked()){
                        try{
                            int freq = Integer.parseInt(specificDateEditText.getText().toString());
                            if(freq >0 && freq < 32){
                                    frequency = specificDateEditText.getText().toString();
                            }else{
                                    new AlertDialog.Builder(ctx)
                                            .setTitle("Invalid input")
                                            .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, null)
                                            .show();
                            }
                        }catch(NumberFormatException e){
                                new AlertDialog.Builder(ctx)
                                        .setTitle("Invalid input")
                                        .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();
                        }

                }

                return frequency;
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

        private void deleteEvent(String eventTitle, String eventFrequency, int eventDuration){
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                dbo.deleteEvent(dbo, eventTitle, eventFrequency, Integer.toString(eventDuration));
        }

        public void reset(){
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                dbo.resetFinished(dbo);
                resetEventsDuration(dbo);
                showNotification();
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

        public void newDay(){
                Calendar c = Calendar.getInstance();
                int thisDay = c.get(Calendar.DAY_OF_YEAR);
                long todayMillis = c.getTimeInMillis();

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(this);
                long last = prefs.getLong("date", System.currentTimeMillis());

                c.setTimeInMillis(last);
                int lastDay = c.get(Calendar.DAY_OF_YEAR);
                // Toast.makeText(getApplicationContext(),
                // "lastday " + lastDay + "thisDay " + thisDay, Toast.LENGTH_LONG)
                // .show();
                if (lastDay == thisDay) {
                        Toast.makeText(MainActivity.this, "Today", Toast.LENGTH_SHORT).show();
                }else{
                        reset();
                }
        }
}
