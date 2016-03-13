package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chris on 11/03/2016.
 */
public class StartScreen  extends Activity {

        Button todayButton, editEventsButton, settingsButton;
        RelativeLayout rl;
        RelativeLayout.LayoutParams lp;
        LinearLayout ll;
        LinearLayout.LayoutParams llp;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            rl = (RelativeLayout)findViewById(R.id.main);
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ll = (LinearLayout)findViewById(R.id.linearLayout);
            llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            startScreen();
      }

        public void startScreen(){
            setContentView(R.layout.start_screen);
            todayButton = (Button)findViewById(R.id.todayButton);
            todayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartScreen.this, MainActivity.class));
                    TableData.TableInfo.EDITING = false;
                }
            });
            editEventsButton = (Button) findViewById(R.id.editEventsButton);
            editEventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartScreen.this, MainActivity.class));
                    TableData.TableInfo.EDITING = true;
                }
            });
        }


}
