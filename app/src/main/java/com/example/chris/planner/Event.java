package com.example.chris.planner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chris on 05/01/2016.
 */
public class Event {

    TextView titleView, durationView, frequencyView;
    Button edit, delete, finished, subtractTime;
    CheckBox done;
    Context ac;
    String title;
    int duration, newDuration;
    boolean frequencyIsANumber = false;
    int frequencyNumber;
//    MainActivity ma;
    LinearLayout linearLayout;
    LinearLayout buttonsLayout;
    public Event(final MainActivity ma, final String title, final String frequency, final int duration){
        this.title = title;
        ma.rl = (RelativeLayout)ma.findViewById(R.id.main);
        ma.lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ma.ll = (LinearLayout)ma.findViewById(R.id.linearLayout);
        ma.llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.editEventScreen(title, frequency, duration);
            }
        });
        edit.setText("edit");

        delete = new Button(ac);
        delete.setText("delete");
        if(!TableData.TableInfo.EDITING){
            finished = new Button(ac);
            finished.setText("finished");
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
        linearLayout.addView(buttonsLayout);
        ma.ll.addView(linearLayout, ma.llp);




    }

    private void subtractTimeScreen(final MainActivity ma){
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
                                        ma.mainScreen();
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
                ma.mainScreen();
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

    private void updateTime(MainActivity ma){
        if(newDuration == 0){
            finishEvent(ma);
        }else{
            DataBaseOperations dbo = new DataBaseOperations(ma);
            dbo.updateTime(dbo, title, Integer.toString(newDuration));
        }

    }

    private void finishEvent(final MainActivity ma){
        new AlertDialog.Builder(ma)
                .setTitle("Event Finished")
                .setMessage("Are you sure you've finished "+ title+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataBaseOperations dbo = new DataBaseOperations(ma);
                        dbo.updateEventFinished(dbo, title);
                        Toast.makeText(ma, "You have finished an event!", Toast.LENGTH_LONG).show();
                        ma.mainScreen();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }
}
