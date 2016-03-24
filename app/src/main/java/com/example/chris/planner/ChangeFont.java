package com.example.chris.planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class ChangeFont extends AppCompatActivity {

    private String[] fonts = {"normal","sans", "serif", "monospace"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_font);
        setupChangeFontScreen();
    }

    private void setupChangeFontScreen(){
//        Button cancelButton, applyButton, resetToDefaultButton;
//        NumberPicker buttonFontPicker, eventFontPicker, titleFontPicker;
//        buttonFontPicker = (NumberPicker) findViewById(R.id.buttonFontPicker);
//        buttonFontPicker.setMinValue(0);
//        buttonFontPicker.setMaxValue(fonts.length - 1);
//        buttonFontPicker.setDisplayedValues(fonts);
//        buttonFontPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Button sampleButton = (Button) findViewById(R.id.fontSampleButton);
//                //Typeface font = getFont(newVal);
//                //sampleButton.setTypeface(font);
//                sampleButton.setText(newVal + " new value");
//            }
//        });
//        eventFontPicker = (NumberPicker) findViewById(R.id.eventFontPicker);
//        eventFontPicker.setMinValue(0);
//        eventFontPicker.setMaxValue(fonts.length - 1);
//        eventFontPicker.setDisplayedValues(fonts);
//        eventFontPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                TextView sampleEventView = (TextView) findViewById(R.id.fontSampleEventView);
//                //Typeface font = getFont(newVal);
//                //sampleButton.setTypeface(font);
//                sampleEventView.setText(newVal + " new value");
//            }
//        });
//        titleFontPicker = (NumberPicker) findViewById(R.id.titleFontPicker);
//        titleFontPicker.setMinValue(0);
//        titleFontPicker.setMaxValue(fonts.length - 1);
//        titleFontPicker.setDisplayedValues(fonts);
//        titleFontPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                TextView sampleTitleView = (TextView) findViewById(R.id.fontSampleTitleView);
//                //Typeface font = getFont(newVal);
//                //sampleButton.setTypeface(font);
//                sampleTitleView.setText(newVal + " new value");
//            }
//        });
//
//
//
//        cancelButton = (Button) findViewById(R.id.fontCancelButton);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ChangeFont.this, SettingsActivity.class));
//            }
//        });
    }

//    private Typeface getFont(int value){
//        if(value == 1){
//
//        }
//        return;
//    }
}
