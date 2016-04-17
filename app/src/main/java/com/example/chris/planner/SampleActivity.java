package com.example.chris.planner;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SampleActivity extends Activity {

    private int next = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        setupHelp();
    }

    private void setupHelp(){
        (findViewById(R.id.helpExitButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button nextButton = (Button) findViewById(R.id.helpNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(next == 1){
                    findViewById(R.id.helpNewButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpNewPointer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpHomeIcon).setVisibility(View.VISIBLE);
                    findViewById(R.id.helpHomePointer).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.helpInstructionsView)).setText("This button takes you to the home screen, where you can" +
                            " view all the events you have today, and edit them any way you see fit!");
                } else if(next == 2){
                    findViewById(R.id.helpHomeIcon).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpHomePointer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpCalendarIcon).setVisibility(View.VISIBLE);
                    findViewById(R.id.helpCalendarPointer).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.helpInstructionsView)).setText("This takes you to the calendar screen. " +
                        "Here you can view what events you will have on any specific day, or events that you have finished/not finished "+
                        "on any previously recorded day.");
                } else if(next == 3){
                    findViewById(R.id.helpCalendarIcon).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpCalendarPointer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpHistoryIcon).setVisibility(View.VISIBLE);
                    findViewById(R.id.helpHistoryPointer).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.helpInstructionsView)).setText("Here you can view all events that you have finished today, "
                        + "all unfinished events from yesterday or search your event database for any specific event.");

                } else if (next == 4){
                    findViewById(R.id.helpHistoryIcon).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpHistoryPointer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.helpSettingsIcon).setVisibility(View.VISIBLE);
                    //findViewById(R.id.helpSettingsPointer).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.helpInstructionsView)).setText("Click here to access various settings; such as "+
                        "transferring events to another device over bluetooth, deleting every event, etc.");
                    next = 0;
                }
                next++;
            }
        });
    }
}
