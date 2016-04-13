package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 03/04/2016.
 */
public class BluetoothExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> headerTitles;
    private HashMap<String, List<String>> childTitles;
    private Activity activity;
    private Context ctx;
    private List<String> timeLeft;
    Typeface font;
    ListPopupWindow listPopupWindow;
    String[] options = {
            "subtract time",
            "move to tomorrow",
            "edit",
            "delete"
    };

    BluetoothExpandableListAdapter(Activity activity, Context ctx, List<String> headerTitles,  HashMap<String, List<String>> childTitles, List<String> timeLeft){

        this.timeLeft = timeLeft;
        this.activity = activity;
        this.ctx = ctx;
        this.childTitles = childTitles;
        this.headerTitles = headerTitles;
        font = Typeface.createFromAsset(ctx.getAssets(), "ComicSansMS.ttf");
        Log.d("STRING",timeLeft.toString());
        Log.d("EVENT NAME", headerTitles.toString());

    }

    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return childTitles.get(headerTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headerTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childTitles.get(headerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String title = (String)this.getGroup(groupPosition);
        final int duration = Integer.parseInt(timeLeft.get(groupPosition));
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_parent_layout, null);
        }
        Log.d("parentview", title);
        TextView textView = (TextView) convertView.findViewById(R.id.eventTextView);
        textView.setTypeface(font, Typeface.BOLD);
        textView.setText(title);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeLeftTextView);
        if(Integer.parseInt(timeLeft.get(groupPosition)) > 0){
            timeTextView.setVisibility(View.VISIBLE);
            timeTextView.setText(timeLeft.get(groupPosition) + " minutes remaining");
            timeTextView.setTypeface(font, Typeface.BOLD);
        }
        ImageView finishedImage = (ImageView) convertView.findViewById(R.id.finishedImageView);
        final View tempView = convertView;
        finishedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(ctx, timeLeft.get(groupPosition) + " pos", Toast.LENGTH_LONG).show();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) tempView.findViewById(R.id.finishedImageView)).setImageResource(R.drawable.finished__icon);
                    if (activity instanceof Version2) {
                        ((Version2) activity).finishedAnEvent(title);
                    }
                }
                return true;
            }
        });
        ImageView editImage = (ImageView) convertView.findViewById(R.id.editEventImageView);
        setListPopup(editImage, title, duration);
        //timeTextView.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String)this.getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bluetooth_child_layout, null);
        }
        Log.d("childview", title);
        TextView textView = (TextView) convertView.findViewById(R.id.childItem);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setText(title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if(getChildrenCount(groupPosition) > 6){
            return childPosition != 1 || childPosition != 0;
        }else if(getChildrenCount(groupPosition) == 6){
            return childPosition != 0;
        }else{
            return true;
        }
    }

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
        final ListPopupWindow listPopupWindow = new ListPopupWindow(ctx);
        listPopupWindow.setAdapter(new ListPopupWindowAdapter(activity, R.layout.more_options_layout, R.id.popTextView, options, imageResources));
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
                ((Version2) activity).setupTodaysEvents();
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
