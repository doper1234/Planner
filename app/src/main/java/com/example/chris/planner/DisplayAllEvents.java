package com.example.chris.planner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class DisplayAllEvents extends AppCompatActivity {

    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_events);
        setupDisplayScreen();
        setColours();
    }

    private void setupDisplayScreen(){
        ll = (LinearLayout) findViewById(R.id.displayMainLayout);
        Button backButton = (Button) findViewById(R.id.displayBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayAllEvents.this, SettingsActivity.class));
            }
        });
        loadEvents();
    }

    private void setColours(){
        findViewById(R.id.displayMainRelativeLayout).setBackgroundColor(Colours.backgroundColour);
        ll.setBackgroundColor(Colours.foregroundColour);
        findViewById(R.id.displayBackButton).setBackgroundColor(Colours.buttonColour);
    }

    private void loadEvents(){
        DataBaseOperations dbo = new DataBaseOperations(this);
        Cursor cr = dbo.getInformation(dbo);
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished, eventInitialDuration;
                int eventDuration;
                eventName = cr.getString(0);
                eventFrequency = cr.getString(1);
                eventDuration = Integer.parseInt(cr.getString(2));
                eventInitialDuration = cr.getString(3);
                //eventFinished = "no";
                new Event(this, eventName, eventFrequency, eventDuration, ll);

            }while(cr.moveToNext());
        }
    }
}
