package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Set;

public class SettingsActivity extends Activity {


    String[] selectionOptions = {
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
    Drawable buttonBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSettingsScreen();
        setupListView();
        //setColours();
    }

    public void setSettingsScreen(){
        findViewById(R.id.changeColourThemeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeColourStatic.class));
            }
        });
        Button changeColourbutton = (Button) findViewById(R.id.changeColourButton);
        changeColourbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeColour.class));
            }
        });
        Drawable background = changeColourbutton.getBackground();
        Button changeFontButton = (Button) findViewById(R.id.changeFontButton);
        buttonBackground = changeFontButton.getBackground();
        //ColorDrawable buttonColor = (ColorDrawable) changeFontButton.getBackground();
        changeFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeFont.class));
            }
        });
        findViewById(R.id.viewAllEventsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, DisplayAllEvents.class));
            }
        });
        findViewById(R.id.transferDataOverBlueToothButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, BluetoothActivity.class));
            }
        });
        Button deleteAllEventsButton = (Button) findViewById(R.id.deleteAllButton);
        deleteAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Delete everything?")
                        .setMessage("Are you sure you want to delete everything??")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                new AlertDialog.Builder(SettingsActivity.this)
                                        .setTitle("Really sure?")
                                        .setMessage("Are you REALLY sure you want to delete everything??")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                new AlertDialog.Builder(SettingsActivity.this)
                                                        .setTitle("All gone")
                                                        .setMessage("All of your events have been erased. Good bye, cruel world!")
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .setPositiveButton(android.R.string.ok, null);
                                                DataBaseOperations dataBaseOperations = new DataBaseOperations(SettingsActivity.this);
                                                dataBaseOperations.deleteDatabase();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        Button backButton = (Button) findViewById(R.id.settingsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, Version2.class));
            }
        });
    }

    private void setColours(){
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.settingsMainLayout);
        Button changeColourbutton = (Button) findViewById(R.id.changeColourButton);
        Button changeFontButton = (Button) findViewById(R.id.changeFontButton);
        Button viewAllEventsButton = (Button) findViewById(R.id.viewAllEventsButton);
        Button sendDataOverBluetooth = (Button) findViewById(R.id.transferDataOverBlueToothButton);
        Button backButton = (Button) findViewById(R.id.settingsBackButton);

        rl.setBackgroundColor(Colours.backgroundColour);
        changeColourbutton.setBackgroundColor(Colours.buttonColour);
        changeFontButton.setBackgroundColor(Colours.buttonColour);
        viewAllEventsButton.setBackgroundColor(Colours.buttonColour);
        sendDataOverBluetooth.setBackgroundColor(Colours.buttonColour);
        backButton.setBackgroundColor(Colours.buttonColour);



    }

    private void setupListView(){
        ListView listView = (ListView) findViewById(R.id.settingsOptionsListView);
        //listView.setBackground(buttonBackground);
        //Log.d("Background", "Back " + buttonBackground);
        //listView.setVisibility(View.INVISIBLE);
//                "Change Colour",
//                "Change Font",
//                "View All Events",
//                "Transfer Data Over Bluetooth",
//                "Change Colour Theme",
//                "Alarm Settings",
//                "Delete all events"
//
        SettingsListAdapter adapter=new SettingsListAdapter(this, selectionOptions, imageIDs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String optionSelected = selectionOptions[position];
                if(optionSelected.equalsIgnoreCase("Change Colour")){
                    startActivity(new Intent(SettingsActivity.this, ChangeColour.class));
                }
                else if(optionSelected.equalsIgnoreCase("Change Font")){
                    startActivity(new Intent(SettingsActivity.this,ChangeFont.class));
                }
                else if(optionSelected.equalsIgnoreCase("View All Events")){
                    startActivity(new Intent(SettingsActivity.this,DisplayAllEvents.class));
                }
                else if(optionSelected.equalsIgnoreCase("Transfer Data Over Bluetooth")){
                    startActivity(new Intent(SettingsActivity.this,BluetoothActivity.class));
                }
                else if(optionSelected.equalsIgnoreCase("Change Colour Theme")){
                    startActivity(new Intent(SettingsActivity.this,ChangeColourStatic.class));
                }
                else if(optionSelected.equalsIgnoreCase("Alarm Settings")){
                    //startActivity(new Intent(SettingsActivity.this, AlarmSettings.class));
                }
                else if(optionSelected.equalsIgnoreCase("Delete all events")){
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("Delete everything?")
                            .setMessage("Are you sure you want to delete everything??")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    new AlertDialog.Builder(SettingsActivity.this)
                                            .setTitle("Really sure?")
                                            .setMessage("Are you REALLY sure you want to delete everything??")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                    new AlertDialog.Builder(SettingsActivity.this)
                                                            .setTitle("All gone")
                                                            .setMessage("All of your events have been erased. Good bye, cruel world!")
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .setPositiveButton(android.R.string.ok, null);
                                                    DataBaseOperations dataBaseOperations = new DataBaseOperations(SettingsActivity.this);
                                                    dataBaseOperations.deleteDatabase();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
                Toast.makeText(SettingsActivity.this, selectionOptions[position], Toast.LENGTH_LONG).show();
            }
        });
    }
}
