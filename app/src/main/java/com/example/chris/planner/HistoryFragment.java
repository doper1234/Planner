package com.example.chris.planner;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Chris on 11/04/2016.
 */
public class HistoryFragment extends Fragment {
    private Context ctx;
    private SlideScreenActivity slideScreenActivity;
    private ViewGroup rootView;
    private boolean searching = false;

    public HistoryFragment(Context context, SlideScreenActivity ssa){
        ctx = context;
        slideScreenActivity = ssa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_previous_history, container, false);
        Button searchButton = (Button) viewID(R.id.historyBackButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSearch();
            }
        });
        Button todayButton = (Button) viewID(R.id.historyTodayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupTodaysFinishedEvents();
            }
        });
        Button yesterdayButton = (Button) viewID(R.id.yesterdayHistoryButton);
        yesterdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupYesterdaysUnfinishedEvents();
            }
        });
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

    private View viewID(int id){
        return rootView.findViewById(id);
    }

    private void setupSearch(){
        ((TextView) viewID(R.id.historyDescriptionTextView)).setText(R.string.search_description);
        ((Button) viewID(R.id.historyTodayButton)).setBackgroundResource(R.drawable.finished_today_icon);
        ((Button) viewID(R.id.yesterdayHistoryButton)).setBackgroundResource(R.drawable.unfinished_yesterday_icon);
        ((Button) viewID(R.id.historyBackButton)).setBackgroundResource(R.drawable.search_icon_pressed);
        LinearLayout linearLayout = (LinearLayout) viewID(R.id.historyLinearLayout);
        linearLayout.removeAllViews();
        EditText searchEditText = (EditText) viewID(R.id.searchEditText);
        searchEditText.setEnabled(true);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                LinearLayout linearLayout = (LinearLayout) viewID(R.id.historyLinearLayout);
                linearLayout.removeAllViews();
                String searchFor = s.toString();
                if (!searchFor.equalsIgnoreCase("")) {
                    DataBaseOperations dbo = new DataBaseOperations(getContext());
                    Cursor dataFound = dbo.getInformation(dbo, searchFor);
                    if (dataFound.moveToFirst()) {
                        do {
                            String title = dataFound.getString(0);
                            TextView titleView = new TextView(getContext());
                            titleView.setText(title);
                            linearLayout.addView(titleView);
                        } while (dataFound.moveToNext());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupTodaysFinishedEvents(){
        ((TextView) viewID(R.id.historyDescriptionTextView)).setText(R.string.finished_events_description);
        ((EditText) viewID(R.id.searchEditText)).setEnabled(false);
        ((Button) viewID(R.id.historyTodayButton)).setBackgroundResource(R.drawable.finished_today_icon_pressed);
        ((Button) viewID(R.id.yesterdayHistoryButton)).setBackgroundResource(R.drawable.unfinished_yesterday_icon);
        ((Button) viewID(R.id.historyBackButton)).setBackgroundResource(R.drawable.search_icon);
        DataBaseOperations dbo = new DataBaseOperations(getContext());
        Cursor dataFound = dbo.getInformation(dbo,TableData.dayOfTheWeek(), TableData.dateOfTheMonth());
        LinearLayout linearLayout = (LinearLayout) viewID(R.id.historyLinearLayout);
        linearLayout.removeAllViews();
        if(dataFound.moveToFirst()){
            do{

                String finished = dataFound.getString(4);
                if(finished.equalsIgnoreCase("yes")){
                    String title = dataFound.getString(0);
                    TextView titleView = new TextView(getContext());
                    titleView.setText(title);
                    linearLayout.addView(titleView);
                }

            }while(dataFound.moveToNext());
        }
    }

    private void setupYesterdaysUnfinishedEvents(){
        ((TextView) viewID(R.id.historyDescriptionTextView)).setText(R.string.unfinished_events_description);
        ((EditText) viewID(R.id.searchEditText)).setEnabled(false);
        ((Button) viewID(R.id.historyTodayButton)).setBackgroundResource(R.drawable.finished_today_icon);
        ((Button) viewID(R.id.yesterdayHistoryButton)).setBackgroundResource(R.drawable.unfinished_yesterday_icon_pressed);
        ((Button) viewID(R.id.historyBackButton)).setBackgroundResource(R.drawable.search_icon);

        DataBaseOperations dbo = new DataBaseOperations(getContext());
        Cursor dataFound = dbo.getInformation(dbo,TableData.yesterdayOfTheWeek(), TableData.yesterdayDateOfTheMonth());
        LinearLayout linearLayout = (LinearLayout) viewID(R.id.historyLinearLayout);
        linearLayout.removeAllViews();
        if(dataFound.moveToFirst()){
            do{

                String finished = dataFound.getString(4);
                if(finished.equalsIgnoreCase("no")){
                    String title = dataFound.getString(0);
                    String duration = dataFound.getString(2);
                    TextView titleView = new TextView(getContext());
                    titleView.setText(title);
                    TextView timeLeftView = new TextView(getContext());
                    timeLeftView.setText(duration);
                    linearLayout.addView(titleView);
                    linearLayout.addView((timeLeftView));
                }
            }while(dataFound.moveToNext());
        }
    }
}
