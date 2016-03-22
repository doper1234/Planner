package com.example.chris.planner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class ChangeColour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_colour);
        setColourScreen();
    }

    private void setColourScreen(){
        final int colourMax = 255;
        final Button sampleButton = (Button) findViewById(R.id.sampleButton);
        final RelativeLayout sampleBackGround = (RelativeLayout) findViewById(R.id.sampleBackground);
        final RelativeLayout sampleForeGround = (RelativeLayout) findViewById(R.id.sampleForeground);
        final SeekBar seekBarRBackground, seekBarGBackground, seekBarBBackground,
                        seekBarRButton, seekBarGButton, seekBarBButton,
                        seekBarRForeground, seekBarGForeground, seekBarBForeground;

        seekBarRButton = (SeekBar) findViewById(R.id.seekBarRButton);
        seekBarGButton = (SeekBar) findViewById(R.id.seekBarGButton);
        seekBarBButton = (SeekBar) findViewById(R.id.seekBarBButton);
        seekBarRBackground = (SeekBar) findViewById(R.id.seekBarRBackground);
        seekBarGBackground = (SeekBar) findViewById(R.id.seekBarGBackground);
        seekBarBBackground = (SeekBar) findViewById(R.id.seekBarBBackground);
        seekBarRForeground = (SeekBar) findViewById(R.id.seekBarRForeground);
        seekBarGForeground = (SeekBar) findViewById(R.id.seekBarGForeground);
        seekBarBForeground = (SeekBar) findViewById(R.id.seekBarBForeground);

        seekBarRButton.setMax(colourMax);
        seekBarRButton.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRButton.getProgress();
                g = seekBarGButton.getProgress();
                b = seekBarBButton.getProgress();
                sampleButton.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarGButton.setMax(colourMax);
        seekBarGButton.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRButton.getProgress();
                g = seekBarGButton.getProgress();
                b = seekBarBButton.getProgress();
                sampleButton.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBButton.setMax(colourMax);
        seekBarBButton.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRButton.getProgress();
                g = seekBarGButton.getProgress();
                b = seekBarBButton.getProgress();
                sampleButton.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarRBackground.setMax(colourMax);
        seekBarRBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRBackground.getProgress();
                g = seekBarGBackground.getProgress();
                b = seekBarBBackground.getProgress();
                sampleBackGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarGBackground.setMax(colourMax);
        seekBarGBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRBackground.getProgress();
                g = seekBarGBackground.getProgress();
                b = seekBarBBackground.getProgress();
                sampleBackGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBBackground.setMax(colourMax);
        seekBarBBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRBackground.getProgress();
                g = seekBarGBackground.getProgress();
                b = seekBarBBackground.getProgress();
                sampleBackGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarRForeground.setMax(colourMax);
        seekBarRForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRForeground.getProgress();
                g = seekBarGForeground.getProgress();
                b = seekBarBForeground.getProgress();
                sampleForeGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarGForeground.setMax(colourMax);
        seekBarGForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRForeground.getProgress();
                g = seekBarGForeground.getProgress();
                b = seekBarBForeground.getProgress();
                sampleForeGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBForeground.setMax(colourMax);
        seekBarBForeground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r, g, b;
                r = seekBarRForeground.getProgress();
                g = seekBarGForeground.getProgress();
                b = seekBarBForeground.getProgress();
                sampleForeGround.setBackgroundColor(setColour(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button backButton = (Button) findViewById(R.id.colourBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeColour.this, SettingsActivity.class));
            }
        });
    }

    private int setColour(int r, int g, int b){

        return Color.rgb(r, g, b);
    }
}
