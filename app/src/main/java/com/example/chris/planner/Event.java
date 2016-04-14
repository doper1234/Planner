package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chris on 05/01/2016.
 */
public class Event {

    TextView titleView, durationView, initialDurationView, finishedView, frequencyView;
    Button edit, delete, finished, subtractTime;
    CheckBox done;
    Context ac;
    String title;
    int duration, newDuration;
    boolean frequencyIsANumber = false;
    int frequencyNumber;
    Activity ma;
//    MainActivity ma;
    LinearLayout linearLayout;
    LinearLayout buttonsLayout;
    public Event(final Activity ma, final String title, final String frequency, final int duration,
                  LinearLayout ll){
        this.title = title;
        this.ma = ma;
//        rl = (RelativeLayout)ma.findViewById(R.id.main);
//        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //ll = (LinearLayout)ma.findViewById(R.id.linearLayout);
        //llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ac = ma.getApplicationContext();
        titleView = new TextView(ac);
        titleView.setText(title + " ");
        titleView.setTextSize(30);
        titleView.setTextColor(Color.BLACK);
        try{
            frequencyNumber = Integer.parseInt(frequency);
            frequencyIsANumber = true;

        }catch(NumberFormatException e){
            frequencyIsANumber = false;
        }
        frequencyView = new TextView(ac);
        if(frequencyIsANumber){
            if(frequencyNumber == 1 || frequencyNumber == 21 || frequencyNumber == 31){
                frequencyView.setText("Every " + frequency + "st of each month");
            }else if(frequencyNumber == 2 || frequencyNumber == 22){
                frequencyView.setText("Every " + frequency + "nd of each month");
            }else{
                frequencyView.setText("Every " + frequency + "th of each month");
            }

        }else{
            frequencyView.setText(frequency + " ");
        }

        frequencyView.setTextColor(Color.BLACK);

        done = new CheckBox(ac);
        edit = new Button(ac);
        edit.setBackgroundColor(Colours.buttonColour);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentEvent.NAME = title;
                CurrentEvent.DAYS = frequency;
                CurrentEvent.DURATION = duration;
                TableData.TableInfo.EDITING = true;
                ma.startActivity(new Intent(ma, NewEventScreen.class));
                //editEventScreen(ma, title, frequency, duration);
            }
        });
        edit.setText("edit");
        edit.setTextColor(Color.BLACK);

        delete = new Button(ac);
        delete.setBackgroundColor(Colours.buttonColour);
        delete.setText("delete");
        delete.setTextColor(Color.BLACK);
        if(!TableData.TableInfo.EDITING){
            finished = new Button(ac);
            finished.setText("finished");
            finished.setTextColor(Color.BLACK);
            finished.setBackgroundColor(Colours.buttonColour);
            finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishEvent(ma);
                }
            });
        }


        buttonsLayout = new LinearLayout(ma);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.addView(edit);
        //buttonsLayout.addView(delete);
        if(!TableData.TableInfo.EDITING){
            buttonsLayout.addView(finished);
        }
        this.duration = duration;

        if(duration > 0) {

            durationView = new TextView(ac);
            if(!TableData.TableInfo.EDITING){
                durationView.setText(duration + " minutes remaining");
                subtractTime = new Button(ac);
                subtractTime.setTextColor(Color.BLACK);
                subtractTime.setBackgroundColor(Colours.buttonColour);
                subtractTime.setText("subtract time");
                subtractTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subtractTimeScreen(ma);
                    }
                });
                buttonsLayout.addView(subtractTime);
            }else{
                durationView.setText(duration + " minutes");
            }

            durationView.setTextColor(Color.BLACK);
        }

        linearLayout = new LinearLayout(ma);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(titleView);
        linearLayout.addView(frequencyView);
        if(durationView != null){
            linearLayout.addView(durationView);
        }

        if(!TableData.TableInfo.DISPLAY_ONLY){
            linearLayout.addView(buttonsLayout);
        }
        //ll.removeAllViews();
        ll.addView(linearLayout);




    }
    public Event(final Activity ma, final String title, final String frequency, final int duration,
                 final String initialDuration, final String finishedState, LinearLayout ll){

        this(ma, title, frequency, duration, ll);

        if(finishedState != null){
            finishedView = new TextView(ac);
            finishedView.setText("Finished: " + finishedState);
        }

        if(initialDuration != null){
            initialDurationView = new TextView(ac);
            initialDurationView.setText("Initial time: " + initialDuration +
                    "Time remaining: " + duration);
        }

        if(initialDurationView != null){
            linearLayout.addView(initialDurationView);
        }
        if(finishedView != null){
            linearLayout.addView(finishedView);
        }

    }

    private void subtractTimeScreen(final Activity ma){
        ma.setContentView(R.layout.subtract_time);
        SeekBar timeToSubtract = (SeekBar) ma.findViewById(R.id.timeToSubtractBar);
        timeToSubtract.setMax(duration);
        timeToSubtract.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = (TextView) ma.findViewById(R.id.subtractAmountView);
                setDuration(seekBar,tv);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button cancel = (Button) ma.findViewById(R.id.subtractCancelButton);
        Button ok = (Button) ma.findViewById(R.id.subtractOkButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ma)
                        .setTitle("Update Time?")
                        .setMessage("Are you sure you want to subtract " + (duration - newDuration) + " from time remaining?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        updateTime(ma);
                                        //ma.mainScreen();
                                        ma.startActivity(new Intent(ma, Version2.class));
                                        Toast.makeText(ma, "Time updated!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();

            }
        });
        TextView subtractTitle = (TextView) ma.findViewById(R.id.subtractTitle);
        subtractTitle.setText(title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ma.mainScreen();
                ma.startActivity(new Intent(ma, Version2.class));
            }
        });
    }

    private void setDuration(SeekBar sb, TextView tv){
        int minutes = sb.getProgress();
        newDuration = duration - minutes;
        int hours = minutes /60;
        if(minutes == 1){
            tv.setText(minutes + " minute");
        }else if (minutes >=60){
            tv.setText(hours + " hour " + minutes % 60 + " minutes");
        }else{
            tv.setText(minutes + " minutes");
        }
    }

    private void updateTime(Activity ma){
        if(newDuration == 0){
            finishEvent(ma);
        }else{
            DataBaseOperations dbo = new DataBaseOperations(ma);
            dbo.updateTime(dbo, title, Integer.toString(newDuration));
        }

    }

    private void finishEvent(final Activity ma){
        new AlertDialog.Builder(ma)
                .setTitle("Event Finished")
                .setMessage("Are you sure you've finished "+ title+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataBaseOperations dbo = new DataBaseOperations(ma);
                        dbo.updateEventFinished(dbo, title);

                        Toast.makeText(ma, "You have finished an event!", Toast.LENGTH_LONG).show();
                        if(ma instanceof Version2){
                            ((Version2) ma).setupTodaysEvents();
                        }else{
                            ma.startActivity(new Intent(ma, Version2.class));
                        }

                        //ma.setContentView(R.layout.activity_version2);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void editEventScreen(final Activity ma, final String title, final String frequency, final int duration){
        ma.setContentView(R.layout.new_event);
        setupCheckBoxListeners();
        Button deleteButton = (Button) ma.findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ma)
                        .setTitle("Delete event?")
                        .setMessage("Delete " + title + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteEvent(title, frequency, duration);
                                ma.setContentView(R.layout.activity_version2);
                                //mainScreen();
                                Toast.makeText(ma, "Event deleted!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        EditText eventTitle = (EditText) ma.findViewById(R.id.eventTitle);
        eventTitle.setText(title);
        setCheckBoxes(frequency);
        final SeekBar timeSeekBar = (SeekBar) ma.findViewById(R.id.seekBar);
        timeSeekBar.setProgress(duration);
        setDuration(ma);
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView timeView = (TextView) ma.findViewById(R.id.timeView);
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
        Button addToPlanner = (Button) ma.findViewById(R.id.addToEventsButton);
        addToPlanner.setText("save");
        addToPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ma)
                        .setTitle("Save event?")
                        .setMessage("Save "+ title+"?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final int duration = timeSeekBar.getProgress();
                                if (!getFrequency().equalsIgnoreCase("")) {
                                    addNewItem(getFrequency(), duration, true);
                                    Toast.makeText(ma, "yes", Toast.LENGTH_LONG);
                                } else {
                                    Toast.makeText(ma,"nope", Toast.LENGTH_LONG);
                                    new AlertDialog.Builder(ma)
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
        Button cancel = (Button) ma.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.setContentView(R.layout.activity_version2);
            }
        });
    }

    private void setDuration(Activity ma){
        TextView timeView = (TextView) ma.findViewById(R.id.timeView);
        SeekBar timeSeekBar = (SeekBar) ma.findViewById(R.id.seekBar);
        int minutes = timeSeekBar.getProgress();
        int hours = minutes /60;
        if(minutes == 1){
            timeView.setText(minutes + " minute");
        }else if (minutes >=60){
            timeView.setText(hours + " hour " + minutes % 60 + " minutes");
        }else{
            timeView.setText(minutes + " " + ma.getResources().getText(R.string.minutes));
        }
    }

    private void setCheckBoxes(String frequency){
        CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
        if(frequency.contains("monday")){
            mondayCheckBox = (CheckBox) ma.findViewById(R.id.mondayCheckBox);
            mondayCheckBox.setChecked(true);
        }
        if(frequency.contains("tuesday")){
            tuesdayCheckBox = (CheckBox) ma.findViewById(R.id.tuesdayCheckBox);
            tuesdayCheckBox.setChecked(true);
        }
        if(frequency.contains("wednesday")){
            wednesdayCheckBox = (CheckBox) ma.findViewById(R.id.wednesdayCheckBox);
            wednesdayCheckBox.setChecked(true);
        }
        if(frequency.contains("thursday")){
            thursdayCheckBox = (CheckBox) ma.findViewById(R.id.thursdayCheckBox);
            thursdayCheckBox.setChecked(true);
        }
        if(frequency.contains("friday")){
            fridayCheckBox = (CheckBox) ma.findViewById(R.id.fridayCheckBox);
            fridayCheckBox.setChecked(true);
        }
        if(frequency.contains("saturday")){
            saturdayCheckBox = (CheckBox) ma.findViewById(R.id.saturdayCheckBox);
            saturdayCheckBox.setChecked(true);
        }
        if(frequency.contains("sunday")){
            sundayCheckBox = (CheckBox) ma.findViewById(R.id.sundayCheckBox);
            sundayCheckBox.setChecked(true);
        }
        try{
            Integer.parseInt(frequency);
            onceAMonthCheckbox = (CheckBox) ma.findViewById(R.id.onceAMonthCheckBox);
            onceAMonthCheckbox.setChecked(true);
            EditText specificDateEditText = (EditText) ma.findViewById(R.id. specificDateEditText);
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
                    new AlertDialog.Builder(ma)
                            .setTitle("Invalid input")
                            .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            }catch(NumberFormatException e){
                new AlertDialog.Builder(ma)
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
        DataBaseOperations dbo = new DataBaseOperations(ma);

        if(editing){
            dbo.updateEventEdited(dbo, title, frequency, Integer.toString(duration));
            ma.setContentView(R.layout.new_event);
            Toast.makeText(ma, "Event saved!", Toast.LENGTH_LONG).show();
        }else{
            if(!dbo.doesEventNameAlreadyExist(dbo, title)){
                dbo.putInformation(dbo, title, frequency, duration);
                ma.setContentView(R.layout.new_event);
                Toast.makeText(ma, "Event saved!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deleteEvent(String eventTitle, String eventFrequency, int eventDuration){
        DataBaseOperations dbo = new DataBaseOperations(ma);
        dbo.deleteEvent(dbo, eventTitle, eventFrequency, Integer.toString(eventDuration));
    }

    private View findViewById(int id){
        return ma.findViewById(id);
    }
}
