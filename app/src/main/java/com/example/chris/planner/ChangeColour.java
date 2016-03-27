package com.example.chris.planner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChangeColour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_colour);
        setColourScreen();
        setColours();
    }

    private void setColourScreen(){
        final int colourMax = 255;
        int buttonR, buttonG, buttonB, foreR, foreG, foreB, backR, backG, backB;
        int[] buttonColours = setRGB(Colours.buttonColour);
        buttonR = buttonColours[0];
        buttonG = buttonColours[1];
        buttonB = buttonColours[2];
        int[] foregroundColours = setRGB(Colours.foregroundColour);
        foreR = foregroundColours[0];
        foreG = foregroundColours[1];
        foreB = foregroundColours[2];
        int[] backgroundColours = setRGB(Colours.backgroundColour);
        backR = backgroundColours[0];
        backG = backgroundColours[1];
        backB = backgroundColours[2];
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
        seekBarRButton.setProgress(buttonR);
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
        seekBarGButton.setProgress(buttonG);
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
        seekBarBButton.setProgress(buttonB);
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
        seekBarRBackground.setProgress(backR);
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
        seekBarGBackground.setProgress(backG);
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
        seekBarBBackground.setProgress(backB);
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
        seekBarRForeground.setProgress(foreR);
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
        seekBarGForeground.setProgress(foreG);
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
        seekBarBForeground.setProgress(foreB);
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
        Button applyButton = (Button) findViewById(R.id.applyButton);
        Log.d("DEFAULT BUTTON COLOUR",applyButton.getCurrentTextColor() + "");
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rB = seekBarRButton.getProgress();
                int gB = seekBarGButton.getProgress();
                int bB = seekBarBButton.getProgress();

                int rF = seekBarRForeground.getProgress();
                int gF = seekBarGForeground.getProgress();
                int bF = seekBarBForeground.getProgress();

                int rBg = seekBarRBackground.getProgress();
                int gBg= seekBarGBackground.getProgress();
                int bBg = seekBarBBackground.getProgress();

                int buttonColour = setColour(rB, gB, bB);
                int foreground = setColour(rF,gF,bF);
                int background =setColour(rBg,gBg,bBg);

                Colours.buttonColour = buttonColour;
                Colours.foregroundColour = foreground;
                Colours.backgroundColour = background;
                setColours();
//                RelativeLayout rl = (RelativeLayout) findViewById(R.id.colourMainLayout);
//                Button applyButton = (Button) findViewById(R.id.applyButton);
//                Drawable background = rl.getBackground();
//                applyButton.setText(rl.getDrawingCacheBackgroundColor() + " color" + background);
            }
        });
        Button defaultsButton = (Button) findViewById(R.id.colourResetToDefaultButton);
        defaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Colours.resetColours();
                setColours();
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

    private void setColours(){
        RelativeLayout backgroundLayout = (RelativeLayout) findViewById(R.id.colourMainLayout);
        Button applyButton = (Button) findViewById(R.id.applyButton);
        Button backButton = (Button) findViewById(R.id.colourBackButton);
        Button resetButton = (Button) findViewById(R.id.colourResetToDefaultButton);

        backgroundLayout.setBackgroundColor(Colours.backgroundColour);
        applyButton.setBackgroundColor(Colours.buttonColour);
        backButton.setBackgroundColor(Colours.buttonColour);
        resetButton.setBackgroundColor(Colours.buttonColour);

        findViewById(R.id.sampleBackground).setBackgroundColor(Color.WHITE);//Colours.backgroundColour);
        findViewById(R.id.sampleForeground).setBackgroundColor(Color.WHITE);//Colours.foregroundColour);
        findViewById(R.id.sampleButton).setBackgroundColor(Color.WHITE);//Colours.buttonColour);
    }

    private int[] setRGB(int colour){
//            List<Integer> digits = new ArrayList<Integer>();
//            while(colour < 0) {
//                digits.add(colour % 10);
//                colour /= 10;
//            }
//            Log.d("LOOK HERE", digits.get(0) + digits.get(1) + digits.get(2) + "");
//
//
//        String rgb = colour + " no?";
//        //char[] rgb = demo.toCharArray();
//        String red = rgb.substring(1,2);// + Character.toString(rgb[2]);
//        String green = rgb.substring(2,3);//Character.toString(rgb[3]) + Character.toString(rgb[4]);
//        String blue = rgb.substring(3,4);//Character.toString(rgb[5]) + Character.toString(rgb [6]);
//        Log.d("RGB", red + green + blue + " ALL:" + colour);
//        int r = Integer.parseInt(red);
//        int g = Integer.parseInt(green);
//        int b = Integer.parseInt(blue);
//        int[] colours = {r,g,b};
        int[] colours = {255,255,255};
        return colours;
    }


    private int setColour(int r, int g, int b){

        return Color.rgb(r, g, b);
    }
}
