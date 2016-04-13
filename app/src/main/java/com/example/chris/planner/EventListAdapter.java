package com.example.chris.planner;

/**
 * Created by Chris on 08/04/2016.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon);
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
                    R.drawable.move_event_to_tomorrow_icon,
                    R.drawable.edit_icon,
                    R.drawable.delete_all_test_icon
            };
            if(duration == 0){
                options = new String[3];
                options[0] = this.options[1];
                options[1] = this.options[2];
                options[2] = this.options[3];
                imageResources = new int[3];
                imageResources[0] = R.drawable.move_event_to_tomorrow_icon;
                imageResources[1] = R.drawable.edit_icon;
                imageResources[2] = R.drawable.delete_all_test_icon;

            }
            final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
            listPopupWindow.setAdapter(new ListPopupWindowAdapter(context, R.layout.more_options_layout, R.id.popTextView, options, imageResources));
            //listPopupWindow.setAdapter(new ArrayAdapter(ctx, R.layout.more_options_layout, R.id.popTextView, options));
            listPopupWindow.setWidth(340);
            listPopupWindow.setHeight(400);

            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(ctx, options[position], Toast.LENGTH_LONG).show();
                    //showSubtractPopup(title);
                    switch (position) {
                        case 0: showSubtractPopup(title, duration);
                            break;
                        case 1:
                            listPopupWindow.dismiss();
                            break;
                        case 2: if (activity instanceof Version2) {
                            //((Version2) activity).de(title);
                        }
                            listPopupWindow.dismiss();
                            break;
                    }
                    listPopupWindow.dismiss();
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
    }


