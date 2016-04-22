package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 21/04/2016.
 */
public class DatabaseEventListAdapter extends ArrayAdapter<String>{
        private final Activity context;
        private List<String> eventNames;
        private List<String> timeLeft;
    private List<String> frequencies;
        private Activity activity;
        private Context ctx;
        Typeface font;

        public DatabaseEventListAdapter(Activity context, List<String> eventNames, List<String> frequencies, List<String> timeLeft) {
            super(context, R.layout.database_event_layout, eventNames);
            this.context = this.activity = context;
            ctx = context;
            this.eventNames = eventNames;
            this.frequencies = frequencies;
            this.timeLeft = timeLeft;
            Log.d("Event name", eventNames.toString());
        }

        public View getView(final int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.database_event_layout, null,true);

            TextView textView = (TextView) rowView.findViewById(R.id.databaseEventTextView);
            textView.setTypeface(font, Typeface.BOLD);
            textView.setText(eventNames.get(position));
            TextView frequencyTextView = (TextView) rowView.findViewById(R.id.databaseEventFrequencyTextView);
            frequencyTextView.setText("every " + frequencies.get(position));
            TextView timeTextView = (TextView) rowView.findViewById(R.id.databaseEventDurationTextView);
            if(Integer.parseInt(timeLeft.get(position)) > 0){
                timeTextView.setVisibility(View.VISIBLE);
                timeTextView.setText(timeLeft.get(position) + " minutes");
                timeTextView.setTypeface(font, Typeface.BOLD);
            }else {
                timeTextView.setVisibility(View.INVISIBLE);
            }
                ImageView editImage = (ImageView) rowView.findViewById(R.id.databaseEventEditImageView);
                final View tempView = rowView;
                editImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.check_dark_blue);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.check_blue);
                        showEventEdit(eventNames.get(position));
                            //finish event
                        }
                        return true;
                    }
                });
                ImageView deleteImage = (ImageView) rowView.findViewById(R.id.databaseEventDeleteImageView);
                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEvent(eventNames.get(position));
                    }
                });
                //setListPopup(editImage, eventNames.get(position), Integer.parseInt(timeLeft.get(position)));


                //timeTextView.setText(title);
            return rowView;

        }

        private void showEventEdit(String title){
            final Dialog yourDialog = new Dialog(activity);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.new_event, (ViewGroup) activity.findViewById(R.id.newEventMainLayout));
            yourDialog.setTitle("Edit " + title);
            yourDialog.setContentView(layout);
            setupEditEvent(yourDialog, title);
            yourDialog.show();
        }

        private void setupEditEvent(final Dialog d, String title){
            //String newTitle;
            setupCheckBoxListeners(d);
            DataBaseOperations dbo = new DataBaseOperations(ctx);
            Cursor eventData = dbo.getInformation(dbo, title);
            String frequency = "";
            int duration = 0;
            if(eventData.moveToFirst()){
                frequency = eventData.getString(1);
                duration = Integer.parseInt(eventData.getString(2));
            }

//            Button deleteButton = (Button) d.findViewById(R.id.deleteButton);
//            deleteButton.setVisibility(View.VISIBLE);
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(ctx)
//                            .setTitle("Delete event?")
//                            .setMessage("Delete " + title + "?")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    deleteEvent(title, frequency, duration);
//                                    startActivity(new Intent(NewEventScreen.this, Version2.class));
//                                    //mainScreen();
//                                    Toast.makeText(ctx, "Event deleted!", Toast.LENGTH_LONG).show();
//                                }
//                            })
//                            .setNegativeButton(android.R.string.no, null).show();
//
//                }
//            });
            EditText eventTitle = (EditText) d.findViewById(R.id.eventTitle);
            eventTitle.setText(title);
            setCheckBoxes(d, frequency);
            final SeekBar timeSeekBar = (SeekBar) d.findViewById(R.id.seekBar);
            timeSeekBar.setProgress(duration);
            setDuration(d);
            timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    TextView timeView = (TextView) d.findViewById(R.id.timeView);
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
            Button addToPlanner = (Button) d.findViewById(R.id.addToEventsButton);
            addToPlanner.setText("save");
            addToPlanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView titleView = (TextView) d.findViewById(R.id.eventTitle);
                    final String newTitle = titleView.getText().toString();
                    new AlertDialog.Builder(ctx)
                            .setTitle("Save event?")
                            .setMessage("Save "+ newTitle+"?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    final int duration = timeSeekBar.getProgress();
                                    if (!getFrequency(d).equalsIgnoreCase("")) {
                                        saveEvent(newTitle, getFrequency(d), duration);
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
            Button cancel = (Button) d.findViewById(R.id.cancelButton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
        }

        private void setDuration(Dialog d){
            TextView timeView = (TextView) d.findViewById(R.id.timeView);
            SeekBar timeSeekBar = (SeekBar) d.findViewById(R.id.seekBar);
            int minutes = timeSeekBar.getProgress();
            int hours = minutes /60;
            if(minutes == 1){
                timeView.setText(minutes + " minute");
            }else if (minutes >=60){
                timeView.setText(hours + " hour " + minutes % 60 + " minutes");
            }else{
                timeView.setText(minutes + " " + ctx.getResources().getText(R.string.minutes));
            }
        }

        private void setCheckBoxes(Dialog d, String frequency){
            CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
            if(frequency.contains("monday")){
                mondayCheckBox = (CheckBox) d.findViewById(R.id.mondayCheckBox);
                mondayCheckBox.setChecked(true);
            }
            if(frequency.contains("tuesday")){
                tuesdayCheckBox = (CheckBox) d.findViewById(R.id.tuesdayCheckBox);
                tuesdayCheckBox.setChecked(true);
            }
            if(frequency.contains("wednesday")){
                wednesdayCheckBox = (CheckBox) d.findViewById(R.id.wednesdayCheckBox);
                wednesdayCheckBox.setChecked(true);
            }
            if(frequency.contains("thursday")){
                thursdayCheckBox = (CheckBox) d.findViewById(R.id.thursdayCheckBox);
                thursdayCheckBox.setChecked(true);
            }
            if(frequency.contains("friday")){
                fridayCheckBox = (CheckBox) d.findViewById(R.id.fridayCheckBox);
                fridayCheckBox.setChecked(true);
            }
            if(frequency.contains("saturday")){
                saturdayCheckBox = (CheckBox) d.findViewById(R.id.saturdayCheckBox);
                saturdayCheckBox.setChecked(true);
            }
            if(frequency.contains("sunday")){
                sundayCheckBox = (CheckBox) d.findViewById(R.id.sundayCheckBox);
                sundayCheckBox.setChecked(true);
            }
            try{
                Integer.parseInt(frequency);
                onceAMonthCheckbox = (CheckBox) d.findViewById(R.id.onceAMonthCheckBox);
                onceAMonthCheckbox.setChecked(true);
                EditText specificDateEditText = (EditText) d.findViewById(R.id.specificDateEditText);
                specificDateEditText.setText(frequency);
                specificDateEditText.setEnabled(true);
            }catch(NumberFormatException e){

            }

        }

        private void setupCheckBoxListeners(Dialog d){
            final CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
            mondayCheckBox = (CheckBox) d.findViewById(R.id.mondayCheckBox);
            tuesdayCheckBox = (CheckBox) d.findViewById(R.id.tuesdayCheckBox);
            wednesdayCheckBox = (CheckBox) d.findViewById(R.id.wednesdayCheckBox);
            thursdayCheckBox = (CheckBox) d.findViewById(R.id.thursdayCheckBox);
            fridayCheckBox = (CheckBox) d.findViewById(R.id.fridayCheckBox);
            saturdayCheckBox = (CheckBox) d.findViewById(R.id.saturdayCheckBox);
            sundayCheckBox = (CheckBox) d.findViewById(R.id.sundayCheckBox);
            onceAMonthCheckbox = (CheckBox) d.findViewById(R.id.onceAMonthCheckBox);
            final EditText eventTitleEditText = (EditText) d.findViewById(R.id.eventTitle);
            eventTitleEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    eventTitleEditText.setText("");
                    return false;
                }
            });
            final EditText specificDateEditText = (EditText) d.findViewById(R.id.specificDateEditText);
            specificDateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    specificDateEditText.setText("");
                }
            });
            mondayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mondayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            tuesdayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tuesdayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            wednesdayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wednesdayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            thursdayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thursdayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            fridayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fridayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            saturdayCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (saturdayCheckBox.isChecked()) {
                        if (onceAMonthCheckbox.isChecked()) {
                            onceAMonthCheckbox.setChecked(false);
                            specificDateEditText.setEnabled(false);
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
                            specificDateEditText.setEnabled(false);
                        }
                    }
                }
            });
            onceAMonthCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onceAMonthCheckbox.isChecked()){
                        specificDateEditText.setEnabled(true);
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

        private String getFrequency(Dialog d){
            String frequency = "";
            CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
            mondayCheckBox = (CheckBox) d.findViewById(R.id.mondayCheckBox);
            tuesdayCheckBox = (CheckBox) d.findViewById(R.id.tuesdayCheckBox);
            wednesdayCheckBox = (CheckBox) d.findViewById(R.id.wednesdayCheckBox);
            thursdayCheckBox = (CheckBox) d.findViewById(R.id.thursdayCheckBox);
            fridayCheckBox = (CheckBox) d.findViewById(R.id.fridayCheckBox);
            saturdayCheckBox = (CheckBox) d.findViewById(R.id.saturdayCheckBox);
            sundayCheckBox = (CheckBox) d.findViewById(R.id.sundayCheckBox);
            onceAMonthCheckbox = (CheckBox) d.findViewById(R.id.onceAMonthCheckBox);
            EditText specificDateEditText = (EditText) d.findViewById(R.id.specificDateEditText);
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

        private void saveEvent(String title, String frequency, int duration){
            DataBaseOperations dbo = new DataBaseOperations(ctx);
            dbo.deleteEvent(dbo, title, frequency, Integer.toString(duration));
            dbo.putInformation(dbo, title, frequency, duration);

        }

        private void deleteEvent(final String title){
            new AlertDialog.Builder(ctx)
                            .setTitle("Delete event?")
                            .setMessage("Delete " + title + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DataBaseOperations dbo = new DataBaseOperations(ctx);
                                    //dbo.deleteEvent(dbo, title);
                                    Toast.makeText(ctx, "Event " + title +  " deleted!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
        }
    }

