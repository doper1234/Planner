package com.example.chris.planner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ListPopupWindow;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



public class SlideScreenActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TodaysEventsFragment todaysEventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_screen);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),5);
        mPager.setAdapter(mPagerAdapter);

        final ImageView helpIcon = (ImageView) findViewById(R.id.helpIcon);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final TabLayout.Tab bookTab = tabLayout.newTab().setText("Today").setIcon(R.drawable.home_icon_pressed);
        final TabLayout.Tab newTab = tabLayout.newTab().setText("Events").setIcon(R.drawable.new_white);
        tabLayout.addTab(newTab);
        tabLayout.addTab(bookTab);
        tabLayout.addTab(tabLayout.newTab().setText("Calendar").setIcon(R.drawable.calendar_white));
        tabLayout.addTab(tabLayout.newTab().setText("History").setIcon(R.drawable.clock_white));
        tabLayout.addTab(tabLayout.newTab().setText("Settings").setIcon(R.drawable.settings_white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == newTab){
                    helpIcon.setImageResource(R.drawable.add_light_no_transparency_center);
                    helpIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newEvent();
                        }
                    });
                }else{
                    helpIcon.setImageResource(R.drawable.help_icon);
                }
//
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.new_event_icon_pressed);
                    tabLayout.getTabAt(1).setIcon(R.drawable.book_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.calendar_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.clock_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.settings_white);
                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.new_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.home_icon_pressed);
                    tabLayout.getTabAt(2).setIcon(R.drawable.calendar_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.clock_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.settings_white);
                } else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.new_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.book_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.calendar_icon_pressed);
                    tabLayout.getTabAt(3).setIcon(R.drawable.clock_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.settings_white);

                } else if (tab.getPosition() == 3) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.new_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.book_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.calendar_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.history_icon_pressed);
                    tabLayout.getTabAt(4).setIcon(R.drawable.settings_white);
                } else if (tab.getPosition() == 4) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.new_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.book_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.calendar_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.clock_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.settings_icon_pressed);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        bookTab.select();
//        mPager.setCurrentItem(0);
//
//        ImageView helpIcon = (ImageView) findViewById(R.id.helpIcon);
//        helpIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SlideScreenActivity.this, SampleActivity.class));
//////                RelativeLayout helpLayout = new RelativeLayout(SlideScreenActivity.this);
//////
//////                helpLayout.setBackgroundResource(R.color.help_dark_colour);
////
////                final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
////                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////                View layout = inflater.inflate(R.layout.help_button_explanation, (ViewGroup)findViewById(R.id.helpRootLayout));
////                yourDialog.setContentView(layout);
////                yourDialog.show();
//            }
//        });
//
//        ImageView homeIcon = (ImageView) findViewById(R.id.homeIcon);
//        homeIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//                homeFragment();
//            }
//        });
//        ImageView calendarIcon = (ImageView) findViewById(R.id.calendarIcon);
//        calendarIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(1);
//                calendarFragment();
//            }
//        });
//        ImageView historyIcon = (ImageView) findViewById(R.id.historyIcon);
//        historyIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(2);
//                historyFragment();
//            }
//        });
//
//        Button newIcon = (Button) findViewById(R.id.newEventIcon);
//        newIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                View layout = inflater.inflate(R.layout.new_event, (ViewGroup)findViewById(R.id.newEventMainLayout));
//                yourDialog.setTitle("Add New Event");
//                yourDialog.setContentView(layout);
//                newEvent(yourDialog);
//                yourDialog.show();
//            }
//        });
//
//        Button settingsIcon = (Button) findViewById(R.id.settingsIcon);
//        settingsIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                View layout = inflater.inflate(R.layout.activity_settings, (ViewGroup)findViewById(R.id.settingsMainLayout));
//                yourDialog.setTitle("Settings");
//                yourDialog.setContentView(layout);
//                setupSettingsListView(yourDialog);
//                yourDialog.show();
//            }
//        });
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    return new EventFragment(SlideScreenActivity.this, SlideScreenActivity.this);
                case 1:
                    TodaysEventsFragment tab1 = new TodaysEventsFragment(SlideScreenActivity.this, SlideScreenActivity.this);
                    return tab1;
                case 2:
                    CalendarFragment tab2 = new CalendarFragment(SlideScreenActivity.this, SlideScreenActivity.this);
                    return tab2;
                case 3:
                    HistoryFragment tab3 = new HistoryFragment(SlideScreenActivity.this, SlideScreenActivity.this);
                    return tab3;
                case 4:
                    //TabFragment3 tab5 = new TabFragment3();
                    return new SettingsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void setupSettingsListView(Dialog d){
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

        final Integer[] imageIDs = {
                R.drawable.colour_test_icon,
                R.drawable.font_test_icon,
                R.drawable.view_all_test_icon,
                R.drawable.bluetooth_test_icon,
                R.drawable.colour_test_icon,
                R.drawable.alarms_test_icon,
                R.drawable.delete_all_test_icon
        };
            ListView listView = (ListView) d.findViewById(R.id.settingsOptionsListView);
            SettingsListAdapter adapter = new SettingsListAdapter(this, selectionOptions, imageIDs);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String optionSelected = selectionOptions[position];
                    if (optionSelected.equalsIgnoreCase("Change Colour")) {
                        final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.activity_change_colour, (ViewGroup)findViewById(R.id.colourMainLayout));
                        yourDialog.setTitle(optionSelected);
                        yourDialog.setContentView(layout);
                        yourDialog.show();
                    } else if (optionSelected.equalsIgnoreCase("Change Font")) {

                    } else if (optionSelected.equalsIgnoreCase("View All Events")) {
                        final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.activity_display_all_events, (ViewGroup)findViewById(R.id.displayMainRelativeLayout));
                        yourDialog.setTitle(optionSelected);
                        yourDialog.setContentView(layout);
                        yourDialog.show();
                    } else if (optionSelected.equalsIgnoreCase("Transfer Data Over Bluetooth")) {
                        final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.activity_bluetooth, (ViewGroup)findViewById(R.id.bluetoothMainLayout));
                        yourDialog.setTitle(optionSelected);
                        yourDialog.setContentView(layout);
                        yourDialog.show();
                    } else if (optionSelected.equalsIgnoreCase("Change Colour Theme")) {
                        final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.activity_change_colour_static, (ViewGroup)findViewById(R.id.changeColourThemeMainLayout));
                        yourDialog.setTitle(optionSelected);
                        yourDialog.setContentView(layout);
                        yourDialog.show();
                    } else if (optionSelected.equalsIgnoreCase("Alarm Settings")) {
                        final Dialog yourDialog = new Dialog(SlideScreenActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.alarm_settings, (ViewGroup)findViewById(R.id.alarmSettingsRootLayout));
                        yourDialog.setTitle(optionSelected);
                        yourDialog.setContentView(layout);
                        yourDialog.show();
                    } else if (optionSelected.equalsIgnoreCase("Delete all events")) {
                        new AlertDialog.Builder(SlideScreenActivity.this)
                                .setTitle("Delete everything?")
                                .setMessage("Are you sure you want to delete everything??")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        new AlertDialog.Builder(SlideScreenActivity.this)
                                                .setTitle("Really sure?")
                                                .setMessage("Are you REALLY sure you want to delete everything??")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int whichButton) {

                                                        new AlertDialog.Builder(SlideScreenActivity.this)
                                                                .setTitle("All gone")
                                                                .setMessage("All of your events have been erased. Good bye, cruel world!")
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        DataBaseOperations dataBaseOperations = new DataBaseOperations(SlideScreenActivity.this);
                                                        dataBaseOperations.deleteDatabase();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                    Toast.makeText(SlideScreenActivity.this, selectionOptions[position], Toast.LENGTH_LONG).show();
                }
            });
        }

    private void newEvent(){
        final Dialog d = new Dialog(SlideScreenActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.new_event, (ViewGroup)findViewById(R.id.newEventMainLayout));
                d.setTitle("Add New Event");
                d.setContentView(layout);

        setupCheckBoxListeners(d);
        final SeekBar timeSeekBar = (SeekBar) d.findViewById(R.id.seekBar);
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView timeView = (TextView) d.findViewById(R.id.timeView);
                int minutes = seekBar.getProgress();
                int hours = minutes / 60;
                if (minutes == 1) {
                    timeView.setText(minutes + " minute");
                } else if (minutes >= 60) {
                    timeView.setText(hours + " hour " + minutes % 60 + " minutes");
                } else {
                    timeView.setText(minutes + " minutes");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button addToPlanner = (Button) d.findViewById(R.id.addToEventsButton);
        addToPlanner.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                EditText titleView = (EditText) d.findViewById(R.id.eventTitle);
                final String title = titleView.getText().toString();
                new AlertDialog.Builder(SlideScreenActivity.this)
                        .setTitle("Add new event?")
                        .setMessage("Add " + title + " to events?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final int duration = timeSeekBar.getProgress();
                                if (!getFrequency(d).equalsIgnoreCase("")) {
                                    DataBaseOperations dbo = new DataBaseOperations(SlideScreenActivity.this);
                                    if (!dbo.doesEventNameAlreadyExist(dbo, title)) {
                                        dbo.putInformation(dbo, title, getFrequency(d), duration);
                                        Toast.makeText(SlideScreenActivity.this, "Event saved!", Toast.LENGTH_LONG).show();
                                        d.dismiss();
                                        todaysEventsFragment.setupTodaysEvents();
                                    } else {
                                        new AlertDialog.Builder(SlideScreenActivity.this)
                                                .setTitle("Event already exists")
                                                .setMessage(title + " is already in the event database.")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, null)
                                                .show();
                                    }

                                    //Toast.makeText(SlideScreenActivity.this, "Event added!", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        Button cancel = (Button) d.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    private void setupCheckBoxListeners(Dialog d){
        final CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
        mondayCheckBox = (CheckBox) d.findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = (CheckBox) d.findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = (CheckBox) d.findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = (CheckBox) d.findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = (CheckBox) d.findViewById(R.id.fridayCheckBox);
        saturdayCheckBox = (CheckBox) d.findViewById(R.id.saturdayCheckBox);
        sundayCheckBox = (CheckBox) d.findViewById(R.id.sundayCheckBox);
        onceAMonthCheckbox = (CheckBox) d.findViewById(R.id.onceAMonthCheckBox);
        final EditText eventTitleEditText = (EditText) d.findViewById(R.id.eventTitle);
        eventTitleEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eventTitleEditText.setText("");
                return false;
            }
        });
        final EditText specificDateEditText = (EditText) d.findViewById(R.id.specificDateEditText);
        specificDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specificDateEditText.setText("");
            }
        });
        mondayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mondayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        tuesdayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuesdayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        wednesdayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wednesdayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        thursdayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thursdayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        fridayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fridayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        saturdayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saturdayCheckBox.isChecked()) {
                    if (onceAMonthCheckbox.isChecked()) {
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        sundayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sundayCheckBox.isChecked()){
                    if(onceAMonthCheckbox.isChecked()){
                        onceAMonthCheckbox.setChecked(false);
                        specificDateEditText.setEnabled(false);
                    }
                }
            }
        });
        onceAMonthCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onceAMonthCheckbox.isChecked()){
                    specificDateEditText.setEnabled(true);
                    if(mondayCheckBox.isChecked()){
                        mondayCheckBox.setChecked(false);
                    }
                    if(tuesdayCheckBox.isChecked()){
                        tuesdayCheckBox.setChecked(false);
                    }
                    if(wednesdayCheckBox.isChecked()){
                        wednesdayCheckBox.setChecked(false);
                    }
                    if(thursdayCheckBox.isChecked()){
                        thursdayCheckBox.setChecked(false);
                    }
                    if(fridayCheckBox.isChecked()){
                        fridayCheckBox.setChecked(false);
                    }
                    if(saturdayCheckBox.isChecked()){
                        saturdayCheckBox.setChecked(false);
                    }
                    if(sundayCheckBox.isChecked()){
                        sundayCheckBox.setChecked(false);
                    }
                }
            }
        });

    }

    private String getFrequency(Dialog d){
        String frequency = "";
        CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox, onceAMonthCheckbox;
        mondayCheckBox = (CheckBox) d.findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = (CheckBox) d.findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = (CheckBox) d.findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = (CheckBox) d.findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = (CheckBox) d.findViewById(R.id.fridayCheckBox);
        saturdayCheckBox = (CheckBox) d.findViewById(R.id.saturdayCheckBox);
        sundayCheckBox = (CheckBox) d.findViewById(R.id.sundayCheckBox);
        onceAMonthCheckbox = (CheckBox) d.findViewById(R.id.onceAMonthCheckBox);
        EditText specificDateEditText = (EditText) d.findViewById(R.id.specificDateEditText);
        mondayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(mondayCheckBox.isChecked()){
            frequency = frequency + "monday ";
        }
        if(tuesdayCheckBox.isChecked()){
            frequency = frequency + "tuesday ";
        }
        if(wednesdayCheckBox.isChecked()){
            frequency = frequency + "wednesday ";
        }
        if(thursdayCheckBox.isChecked()){
            frequency = frequency + "thursday ";
        }
        if(fridayCheckBox.isChecked()){
            frequency = frequency + "friday ";
        }
        if(saturdayCheckBox.isChecked()){
            frequency = frequency + "saturday ";
        }
        if(sundayCheckBox.isChecked()){
            frequency = frequency + "sunday ";
        }
        if(onceAMonthCheckbox.isChecked()){
            try{
                int freq = Integer.parseInt(specificDateEditText.getText().toString());
                if(freq >0 && freq < 32){
                    frequency = specificDateEditText.getText().toString();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("Invalid input")
                            .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            }catch(NumberFormatException e){
                new AlertDialog.Builder(this)
                        .setTitle("Invalid input")
                        .setMessage(specificDateEditText.getText().toString() + " is not a valid date.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }

        }

        return frequency;
    }

//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            Toast.makeText(SlideScreenActivity.this, "Position: " + position, Toast.LENGTH_LONG).show();
//            if(position == 0){
//                homeFragment();
//                todaysEventsFragment = new TodaysEventsFragment(SlideScreenActivity.this, SlideScreenActivity.this);
//                return todaysEventsFragment;
//            }else if (position == 1){
//                calendarFragment();
//                return new CalendarFragment(SlideScreenActivity.this, SlideScreenActivity.this);
//            }else{
//                historyFragment();
//                return new HistoryFragment(SlideScreenActivity.this, SlideScreenActivity.this);
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//
//
//    }

//    public void homeFragment(){
//        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon_pressed);
//        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon);
//        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon);
//    }
//
//    public void calendarFragment(){
//        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon);
//        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon);
//        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon_pressed);
//    }
//
//    public void historyFragment(){
//        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon);
//        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon);
//        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon_pressed);
//    }

}

