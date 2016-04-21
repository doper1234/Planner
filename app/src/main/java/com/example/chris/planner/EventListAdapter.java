package com.example.chris.planner;

/**
 * Created by Chris on 08/04/2016.
 */
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

public class EventListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<String> eventNames;
    private Activity activity;
    private Context ctx;
    private List<String> timeLeft;
    private TodaysEventsFragment todayFragment;
    Typeface font;
    String[] options = {
            "subtract time",
            "move up",
            "move down",
            "move to tomorrow",
            "edit",
            "delete"
    };

    public EventListAdapter(TodaysEventsFragment fragment, Activity context, List<String> eventNames, List<String> timeLeft) {
        super(context, R.layout.event_parent_layout, eventNames);


        todayFragment = fragment;
        this.context = this.activity = context;
        ctx = context;
        this.eventNames = eventNames;
        this.timeLeft = timeLeft;
        Log.d("Event name", eventNames.toString());
    }

    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.event_parent_layout, null,true);

//        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
//
//        txtTitle.setText(itemname[position]);
//        imageView.setImageResource(imgid[position]);
//        extratxt.setText("Description "+itemname[position]);

        TextView textView = (TextView) rowView.findViewById(R.id.eventTextView);
        textView.setTypeface(font, Typeface.BOLD);
        textView.setText(eventNames.get(position));
        TextView timeTextView = (TextView) rowView.findViewById(R.id.timeLeftTextView);
        if(Integer.parseInt(timeLeft.get(position)) > 0){
            timeTextView.setVisibility(View.VISIBLE);
            timeTextView.setText(timeLeft.get(position) + " minutes remaining");
            timeTextView.setTypeface(font, Typeface.BOLD);
        }
        ImageView finishedImage = (ImageView) rowView.findViewById(R.id.finishedImageView);
        final View tempView = rowView;
        finishedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(ctx, timeLeft.get(position) + " pos", Toast.LENGTH_LONG).show();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.check_dark_blue);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.check_blue);
                    todayFragment.finishEvent(eventNames.get(position));
                    //finish event
                }
                return true;
            }
        });
        ImageView editImage = (ImageView) rowView.findViewById(R.id.editEventImageView);
        setListPopup(editImage, eventNames.get(position), Integer.parseInt(timeLeft.get(position)));


        //timeTextView.setText(title);
        return rowView;

    };

        private void setListPopup(final ImageView t, final String title, final int duration){
            String[] options = this.options;
            int[] imageResources = {
                    R.drawable.subtract_time_icon,
                    R.drawable.move_up_icon,
                    R.drawable.move_down_icon,
                    R.drawable.move_event_to_tomorrow_icon,
                    R.drawable.edit_blue,
                    R.drawable.delete_all_test_icon
            };
            if(duration == 0){
                options = new String[5];
                options[0] = this.options[1];
                options[1] = this.options[2];
                options[2] = this.options[3];
                options[3] = this.options[4];
                options[4] = this.options[5];
                imageResources = new int[5];
                imageResources[0] = R.drawable.move_up_icon;
                imageResources[1] = R.drawable.move_down_icon;
                imageResources[2] = R.drawable.move_event_to_tomorrow_icon;
                imageResources[3] = R.drawable.edit_blue;
                imageResources[4] = R.drawable.delete_all_test_icon;

            }
            final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
            listPopupWindow.setAdapter(new ListPopupWindowAdapter(context, R.layout.more_options_layout, R.id.popTextView, options, imageResources));
            //listPopupWindow.setAdapter(new ArrayAdapter(ctx, R.layout.more_options_layout, R.id.popTextView, options));
            listPopupWindow.setWidth(340);
            listPopupWindow.setHeight(450);

            final String firstOption = options[0];
            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(ctx, options[position], Toast.LENGTH_LONG).show();
                    //showSubtractPopup(title);
                    if (firstOption.equalsIgnoreCase("subtract time")) {
                        switch (position) {
                            case 0:
                                showSubtractPopup(title, duration);
                                listPopupWindow.dismiss();
                                break;

                            case 3:
                                showMoveEventToTomorrow(title);
                                listPopupWindow.dismiss();
                                break;

                            case 4:
                                showEventEdit(title);
                                listPopupWindow.dismiss();
                                break;

                            case 5:
                                deleteEvent(title);
                                listPopupWindow.dismiss();
                                break;
                        }
                    } else {
                        switch (position) {
                            case 0:
                                showMoveEventToTomorrow(title);
                                listPopupWindow.dismiss();
                                break;

                            case 3:
                                showEventEdit(title);
                                listPopupWindow.dismiss();
                                break;

                            case 4:
                                deleteEvent(title);
                                listPopupWindow.dismiss();
                                break;
                        }
                    }

                }
            });

            t.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listPopupWindow.setAnchorView(t);
                    listPopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
                    listPopupWindow.show();
                }
            });
        }

        private void showSubtractPopup(final String title, int duration){

            final int intDuration = duration;
            final Dialog yourDialog = new Dialog(ctx);
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.subtract_time_layout, (ViewGroup)activity.findViewById(R.id.subtractRootElement));
            yourDialog.setTitle("Subtract time?");
            yourDialog.setContentView(layout);

            final SeekBar subtractSeekBar = (SeekBar) yourDialog.findViewById(R.id.subtractSeekBar);

            subtractSeekBar.setMax(duration);
            subtractSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ((TextView) yourDialog.findViewById(R.id.timeToSubtractTextView)).setText("Subtract" + progress + " minutes");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //TextView durationView = (TextView) yourDialog.findViewById(R.id.timeToSubtractTextView);
            ((Button) yourDialog.findViewById(R.id.subtractOkButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newDuration = intDuration - subtractSeekBar.getProgress();
                    if(newDuration == 0){
                        DataBaseOperations dbo = new DataBaseOperations(activity);
                        dbo.updateEventFinished(dbo, title);
                        Toast.makeText(ctx, "Event finished!", Toast.LENGTH_LONG).show();
                    }else{
                        DataBaseOperations dbo = new DataBaseOperations(activity);
                        dbo.updateTime(dbo, title, Integer.toString(newDuration));
                    }
                    todayFragment.setupTodaysEvents();
                    yourDialog.dismiss();
                }
            });

//        ((Button) yourDialog.findViewById(R.id.subtractCancelButton)).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon_pressed);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon);
//
//
//            }
//        });

            yourDialog.show();
//        AlertDialog a = new AlertDialog.Builder(ctx)
//                .setTitle("Subtract Time")
//                .setMessage("How much time?")
//                .setIcon(R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//                })
//                .setNegativeButton(android.R.string.no, null).show();

            //PopupWindow popupMessage = new PopupWindow(ctx);
            //popupMessage.setContentView();

//        String [] titles = {
//          title
//        };
//        ListPopupWindow subtractListPopupWindow = new ListPopupWindow(ctx);
//        subtractListPopupWindow.setAdapter(new ArrayAdapter(ctx, R.layout.subtract_time_layout, R.id.subtractTimeTextView, titles));

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

        private void showMoveEventToTomorrow(String title){
            new AlertDialog.Builder(activity)
                        .setTitle("Move event")
                        .setMessage("Move " + title + " to tomorrow?")
                        .setIcon(R.drawable.ic_dialog_alert)
                        .setPositiveButton("forever", null)
                        .setNegativeButton("once", null)
                        .setNeutralButton("cancel", null)

                        .show();
        }

        private void deleteEvent(String title){

        }
    }


