package com.example.chris.planner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Chris on 20/04/2016.
 */
public class SettingsFragment extends Fragment {

    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_settings, container, false);


        setupListView();
        return rootView;
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.todaysEventsFragment);
//        TableData.TableInfo.EDITING = false;
//        Intent alarmIntent = new Intent(Version2.this, UnfinishedEventsReminderReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(Version2.this, 0, alarmIntent, 0);
//
//        Intent bootIntent = new Intent(Version2.this, ResetFinishedEventsReceiver.class);
//        pendingBootIntent = PendingIntent.getBroadcast(Version2.this, 0, bootIntent, 0);
//
//        TextView text = (TextView) findViewById(R.id.textView2);
//        Typeface font = Typeface.createFromAsset(getAssets(), "ComicSansMS.ttf");
//        text.setTypeface(font);
//        //startAlarmAtSpecificTime(12, 12, 1);
//        startAlarmManager();
//        startResetAlarmManager();
//        createStartScreen();
//        setTabHost();
        //setTabHost();
        //showBradley();
        //setColours();

    }

    private View findViewById(int id){
        return rootView.findViewById(id);
    }

    private void setupListView(){
        ListView listView = (ListView) findViewById(R.id.settingsOptionsListView);
        final String[] selectionOptions = {
                "Change Colour",
                "Change Font",
                "View All Events",
                "Transfer Data Over Bluetooth",
                "Change Colour Theme",
                "Alarm Settings",
                "Delete all events"
//            getString(R.string.change_colour),
//            getString(R.string.change_font),
//            getString(R.string.view_all_events),
//            getString(R.string.transfer_data_over_bluetooth),
//            getString(R.string.change_theme_button),
//            getString(R.string.alarm_button),
//            getString(R.string.delete_button)
        };

        Integer[] imageIDs = {
                R.drawable.colour_test_icon,
                R.drawable.font_test_icon,
                R.drawable.view_all_test_icon,
                R.drawable.bluetooth_test_icon,
                R.drawable.colour_test_icon,
                R.drawable.alarms_test_icon,
                R.drawable.delete_all_test_icon
        };

        SettingsListAdapter adapter=new SettingsListAdapter(getActivity(), selectionOptions, imageIDs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String optionSelected = selectionOptions[position];
                if(optionSelected.equalsIgnoreCase("Change Colour")){
                    //startActivity(new Intent(SettingsActivity.this, ChangeColour.class));
                }
                else if(optionSelected.equalsIgnoreCase("Change Font")){
                    //startActivity(new Intent(SettingsActivity.this,ChangeFont.class));
                }
                else if(optionSelected.equalsIgnoreCase("View All Events")){
                    //startActivity(new Intent(SettingsActivity.this,DisplayAllEvents.class));
                }
                else if(optionSelected.equalsIgnoreCase("Transfer Data Over Bluetooth")){
                    //startActivity(new Intent(SettingsActivity.this,BluetoothActivity.class));
                }
                else if(optionSelected.equalsIgnoreCase("Change Colour Theme")){
                    //startActivity(new Intent(SettingsActivity.this,ChangeColourStatic.class));
                }
                else if(optionSelected.equalsIgnoreCase("Alarm Settings")){
                    //startActivity(new Intent(SettingsActivity.this, AlarmSettings.class));
                }
                else if(optionSelected.equalsIgnoreCase("Delete all events")){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete everything?")
                            .setMessage("Are you sure you want to delete everything??")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Really sure?")
                                            .setMessage("Are you REALLY sure you want to delete everything??")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                    new AlertDialog.Builder(getContext())
                                                            .setTitle("All gone")
                                                            .setMessage("All of your events have been erased. Good bye, cruel world!")
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .setPositiveButton(android.R.string.ok, null);
                                                    DataBaseOperations dataBaseOperations = new DataBaseOperations(getContext());
                                                    dataBaseOperations.deleteDatabase();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
                //Toast.makeText(SettingsActivity.this, selectionOptions[position], Toast.LENGTH_LONG).show();
            }
        });
    }
}
