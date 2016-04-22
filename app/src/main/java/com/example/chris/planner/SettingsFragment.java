package com.example.chris.planner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
//                "Change Colour",
                "Change Font",
//                "View All Events",
                "Transfer Data",// Over Bluetooth",
                "Wifi Sync",
                "Change Colour",// Theme",
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
               // R.drawable.colour_palette_blue,
                R.drawable.font_blue,
              //  R.drawable.view_all_blue,
                R.drawable.bluetooth_blue,
                R.drawable.sync_dark_blue,
                R.drawable.colour_palette_blue,
                R.drawable.alarm_blue,
                R.drawable.delete_all_test_icon
        };

        Integer[] tabImageIDs = {
              //  R.drawable.colour_white,
                R.drawable.font_dark_blue,
                //R.drawable.view_all_white,
                R.drawable.bluetooth_white,
                R.drawable.sync_white,
                R.drawable.colour_white,
                R.drawable.alarm_white,
                R.drawable.delete_white
        };

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.settingsTabLayout);
        for(int i =0; i < selectionOptions.length; i++){
            tabLayout.addTab(tabLayout.newTab().setText(selectionOptions[i]).setIcon(tabImageIDs[i]));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager pager = (ViewPager) findViewById(R.id.settingsPager);
        final PagerAdapter pagerAdapter = new PagerAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());

                pager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_dark_blue);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_white);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_white);

                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_dark_blue);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_white);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_white);

                } else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_dark_blue);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_white);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_white);

                }  else if (tab.getPosition() == 3) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_palette_dark_blue);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_white);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_white);

                } else if (tab.getPosition() == 4) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_dark_blue);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_white);

                } else if (tab.getPosition() == 5) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.font_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.bluetooth_white);
                    tabLayout.getTabAt(2).setIcon(R.drawable.sync_white);
                    tabLayout.getTabAt(3).setIcon(R.drawable.colour_white);
                    tabLayout.getTabAt(4).setIcon(R.drawable.alarm_white);
                    tabLayout.getTabAt(5).setIcon(R.drawable.delete_all_test_icon);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        SettingsListAdapter settingsListAdapter=new SettingsListAdapter(getActivity(), selectionOptions, imageIDs);
        listView.setAdapter(settingsListAdapter);
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

                    return new FontFragment();
                case 1:

                    return new BluetoothFragment();
                case 2:
                    return new SyncWithServerFragment();
                case 3:
                    return new ChangeColourFragment();
                case 4:
                    return new AlarmFragment();
                case 5:
                    //TabFragment3 tab5 = new TabFragment3();
                    return new DeleteFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
