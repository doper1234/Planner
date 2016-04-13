package com.example.chris.planner;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chris on 11/04/2016.
 */
public class CalendarFragment extends Fragment {

    private Context ctx;
    private SlideScreenActivity slideScreenActivity;
    private ViewGroup rootView;

    public CalendarFragment(Context context, SlideScreenActivity ssa){
        ctx = context;
        slideScreenActivity = ssa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.calendar_activity, container, false);

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
}
