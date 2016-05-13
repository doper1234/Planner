package com.example.chris.planner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Chris on 22/04/2016.
 */
public class AlarmFragment extends Fragment {

    ViewGroup rootView;
    private int minute, hour;
    PendingIntent pendingIntent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alarm_settings, container, false);
        final TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        rootView.findViewById(R.id.alarmSettingsApplyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minute = timePicker.getCurrentMinute();
                hour = timePicker.getCurrentHour();
                setAlarm();
            }
        });
        ((Switch)rootView.findViewById(R.id.alarmSettingsSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String alarmSwitchStatus;
                if (isChecked) {
                    alarmSwitchStatus = "Alarm turned on";
                    setAlarm();
                } else {
                    alarmSwitchStatus = "Alarm turned off";
                    cancelAlarm();
                }
                Toast.makeText(getContext(), alarmSwitchStatus, Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.alarmNowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNow();
            }
        });


        return rootView;
    }

    public void setAlarm(){
        Toast.makeText(getContext(), "Alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        Calendar alarmCalender = calendar;
        alarmCalender.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalender.set(Calendar.MINUTE, minute);
        alarmCalender.set(Calendar.SECOND,0);

        Intent alarmIntent = new Intent(getActivity(), UnfinishedEventsReminderReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);

//        long time24h = 24*60*60*1000;
//        long timeAt09_00 = ...; // calculate from now...
//        long timeAt11_30 = ...; // calculate from now...
//
//        alarmMgr1.setInexactRepeating(AlarmManager.RTC_WAKEUP, now ,        time24h, alarmIntent);
//        alarmMgr2.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt09_00, time24h, alarmIntent);
//        alarmMgr3.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAt11_30, time24h, alarmIntent);

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        int interval = 1000*60*60*24;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), interval, pendingIntent);
        //Toast.makeText(this, "Alarm Set for 13:30", Toast.LENGTH_SHORT).show();

    }

    public void setNow(){
        Calendar calendar = Calendar.getInstance();
        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        timePicker.setCurrentMinute(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));

    }

    public void cancelAlarm() {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(getContext(), "Alarm turned off", Toast.LENGTH_SHORT).show();
    }
}
