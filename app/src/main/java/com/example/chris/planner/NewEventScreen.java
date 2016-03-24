package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chris on 18/03/2016.
 */
public class NewEventScreen extends Activity {

    Context ctx = this;
    String newTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        if(TableData.TableInfo.EDITING){
            setupEditEventScreen(CurrentEvent.NAME, CurrentEvent.DAYS, CurrentEvent.DURATION);
        }else{
            newEventScreen();
        }
        setColours();
    }

//    public void setNewEventScreen(){
//        Button addToPlanner = (Button) findViewById(R.id.addToEventsButton);
//        addToPlanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(NewEventScreen.this, Version2.class));
//            }
//        });
//        Button backButton = (Button) findViewById(R.id.cancelButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(NewEventScreen.this, Version2.class));
//            }
//        });
//    }

    private void setColours(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.newEventMainLayout);
        Button addToPlanner = (Button) findViewById(R.id.addToEventsButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        ll.setBackgroundColor(Colours.backgroundColour);
        addToPlanner.setBackgroundColor(Colours.buttonColour);
        cancelButton.setBackgroundColor(Colours.buttonColour);
        deleteButton.setBackgroundColor(Colours.buttonColour);
    }

    public void newEventScreen(){
        setContentView(R.layout.new_event);
        setupCheckBoxListeners();
        final SeekBar timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
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
        Button addToPlanner = (Button) findViewById(R.id.addToEventsButton);
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
                                    startActivity(new Intent(NewEventScreen.this, Version2.class));
                                    Toast.makeText(ctx, "Event added!", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewEventScreen.this, Version2.class));
            }
        });
    }

    public void setupEditEventScreen(final String title, final String frequency, final int duration){
        setContentView(R.layout.new_event);
        setupCheckBoxListeners();
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Delete event?")
                        .setMessage("Delete " + title + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteEvent(title, frequency, duration);
                                startActivity(new Intent(NewEventScreen.this, Version2.class));
                                //mainScreen();
                                Toast.makeText(ctx, "Event deleted!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        EditText eventTitle = (EditText) findViewById(R.id.eventTitle);
        eventTitle.setText(title);
        setCheckBoxes(frequency);
        final SeekBar timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
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
        Button addToPlanner = (Button) findViewById(R.id.addToEventsButton);
        addToPlanner.setText("save");
        addToPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleView = (TextView) findViewById(R.id.eventTitle);
                newTitle = titleView.getText().toString();
                new AlertDialog.Builder(ctx)
                        .setTitle("Save event?")
                        .setMessage("Save "+ newTitle+"?")
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
        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewEventScreen.this, Version2.class));
            }
        });
    }

    private void setDuration(){
        TextView timeView = (TextView) findViewById(R.id.timeView);
        SeekBar timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
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
                if (mondayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
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

    private void deleteEvent(String eventTitle, String eventFrequency, int eventDuration){
        DataBaseOperations dbo = new DataBaseOperations(this);
        dbo.deleteEvent(dbo, eventTitle, eventFrequency, Integer.toString(eventDuration));
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
                    new AlertDialog.Builder(this)
                            .setTitle("Invalid input")
                            .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            }catch(NumberFormatException e){
                new AlertDialog.Builder(this)
                        .setTitle("Invalid input")
                        .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }

        }

        return frequency;
    }

    private void addNewItem(String frequency, int duration, boolean editing){
        EditText titleView = (EditText)findViewById(R.id.eventTitle);
        String title = titleView.getText().toString();
        DataBaseOperations dbo = new DataBaseOperations(this);

        if(editing){
            dbo.updateEventEdited(this, dbo, newTitle, frequency, Integer.toString(duration));
            startActivity(new Intent(this, Version2.class));
            Toast.makeText(this, "Event saved!", Toast.LENGTH_LONG).show();
        }else{
            if(!dbo.doesEventNameAlreadyExist(dbo, title)){
                dbo.putInformation(this, dbo, title, frequency, duration);
                startActivity(new Intent(this, Version2.class));
                Toast.makeText(this, "Event saved!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
