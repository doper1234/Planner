package com.example.chris.planner;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

        Context ctx = this;
        SeekBar timeSeekBar;
        Button add, addToPlanner, cancel;
        ScrollView mainScrollView;
        TextView dateView;
        RelativeLayout rl;
        RelativeLayout.LayoutParams lp;
        LinearLayout ll;
        LinearLayout.LayoutParams llp;
        String file_name = "stored_events";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

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
                        @Override
                        public void onClick(View v) {
                                String eventName = "";
                                TextView timeView = (TextView) findViewById(R.id.timeView);
                                final int duration = timeSeekBar.getProgress();
                                //now ask if the user wants to add this item
//                                new AlertDialog.Builder(MainActivity.this)
//                                        .setTitle("Do you want to add this to your events?")
//                                        .setMessage(eventName +" every " + frequency + " for " + duration)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                                public void onClick(DialogInterface dialog, int whichButton) {
//                                                        //add item and return to main screen
                                                        addNewItem(getFrequency(), duration);
                                                        mainScreen();
//                                                }})
//                                        .setNegativeButton(android.R.string.no, null).show();


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
                Button deleteButton = (Button) findViewById(R.id.deleteButton);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                deleteEvent(title, frequency, duration);
                                mainScreen();
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
                                String eventName = "";
                                TextView timeView = (TextView) findViewById(R.id.timeView);
                                final int duration = timeSeekBar.getProgress();
                                //now ask if the user wants to add this item
//                                new AlertDialog.Builder(MainActivity.this)
//                                        .setTitle("Do you want to add this to your events?")
//                                        .setMessage(eventName +" every " + frequency + " for " + duration)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                                public void onClick(DialogInterface dialog, int whichButton) {
//                                                        //add item and return to main screen
                                addNewItem(getFrequency(), duration);
                                mainScreen();
//                                                }})
//                                        .setNegativeButton(android.R.string.no, null).show();


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
                        timeView.setText(minutes + " minutes");
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
        }

        public void mainScreen(){
                setContentView(R.layout.activity_main);
                DateFormat df = new SimpleDateFormat("EEEE, LLLL d, yyyy");
                Calendar calobj = Calendar.getInstance();
                dateView = (TextView) findViewById(R.id.dateView);
                dateView.setText(df.format(calobj.getTime()));
                add = (Button) findViewById(R.id.addButton);
                add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                newEventScreen();
                        }
                });
                loadEvents();
        }

        private void addNewItem(String frequency, int duration){
                EditText titleView = (EditText)findViewById(R.id.eventTitle);
                String title = titleView.getText().toString();
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                dbo.putInformation(dbo, title, frequency, duration);
        }

        private void loadEvents() {
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                Cursor cr = dbo.getInformation(dbo);
                cr.moveToFirst();

                if(cr.moveToNext()){
                        do{
                                String eventName, eventFrequency;
                                int eventDuration;

                                eventName = cr.getString(0);
                                eventFrequency = cr.getString(1);
                                eventDuration = Integer.parseInt(cr.getString(2));
                                new Event(this, eventName, eventFrequency, eventDuration);
                        }while(cr.moveToNext());
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
                        frequency = specificDateEditText.getText().toString();
                }

                return frequency;
        }

        private void deleteEvent(String eventTitle, String eventFrequency, int eventDuration){
                DataBaseOperations dbo = new DataBaseOperations(ctx);
                dbo.deleteEvent(dbo, eventTitle, eventFrequency, Integer.toString(eventDuration));
        }
}
