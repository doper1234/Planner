package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSettingsScreen();
        setColours();
    }

    public void setSettingsScreen(){
        Button changeColourbutton = (Button) findViewById(R.id.changeColourButton);
        changeColourbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeColour.class));
            }
        });
        Button changeFontButton = (Button) findViewById(R.id.changeFontButton);
        changeFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeFont.class));
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
                                                        .setPositiveButton(android.R.string.ok,null);
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
}
