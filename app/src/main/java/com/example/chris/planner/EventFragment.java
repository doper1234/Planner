package com.example.chris.planner;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 21/04/2016.
 */
public class EventFragment extends Fragment {

    private Context ctx;
    private Activity activity;

    public EventFragment(Context c, Activity a){
        ctx = c;
        activity = a;
    }
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.event_fragment_layout, container, false);
        loadEvents();
        setupSearch();
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

    public void loadEvents(){
        DataBaseOperations dbo = new DataBaseOperations(ctx);
        Cursor cr = dbo.getInformation(dbo);
        ListView listView = (ListView) findViewById(R.id.eventListView);
        List<String> events = new ArrayList<String>();
        List<String> frequencies = new ArrayList<>();
        List<String> durations = new ArrayList<>();
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished;
                String eventDuration;
                eventName = cr.getString(0);
                eventFrequency = cr.getString(1);
                eventDuration = (cr.getString(2));

                events.add(eventName);
                frequencies.add(eventFrequency);
                durations.add(eventDuration);
                    //events.add(eventName + " to do every " + eventFrequency + " for " + eventDuration + " minutes");


            } while (cr.moveToNext());
        }else{
            events.add("You have no events. Why don't you add some?");
        }

        listView.setAdapter(new DatabaseEventListAdapter(getActivity(), events, frequencies, durations));
        //listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.database_event_layout, R.id.databaseEventTextView, events));
    }

    private void setupSearch(){
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListView listView = (ListView) findViewById(R.id.eventListView);
                List<String> events = new ArrayList<>();
                List<String> frequencies = new ArrayList<>();
                List<String> durations = new ArrayList<>();
                if (!newText.equalsIgnoreCase("")) {
                    DataBaseOperations dbo = new DataBaseOperations(getContext());
                    Cursor dataFound = dbo.getInformation(dbo, newText);
                    if (dataFound.moveToFirst()) {
                        do {

                            String eventName, eventFrequency, eventDuration;
                            eventName = dataFound.getString(0);
                            eventFrequency = dataFound.getString(1);
                            eventDuration = (dataFound.getString(2));

                            events.add(eventName);
                            frequencies.add(eventFrequency);
                            durations.add(eventDuration);
                        } while (dataFound.moveToNext());
                    }
                    listView.setAdapter(new DatabaseEventListAdapter(getActivity(), events, frequencies, durations));
                }else{
                    loadEvents();
                }

                return true;
            }
        });
//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                ListView listView = (ListView) viewID(R.id.historyListView);
//                List<String> events = new ArrayList<String>();
//                LinearLayout linearLayout = (LinearLayout) viewID(R.id.historyLinearLayout);
//                linearLayout.removeAllViews();
//                String searchFor = s.toString();
//                if (!searchFor.equalsIgnoreCase("")) {
//                    DataBaseOperations dbo = new DataBaseOperations(getContext());
//                    Cursor dataFound = dbo.getInformation(dbo, searchFor);
//                    if (dataFound.moveToFirst()) {
//                        do {
//                            String title = dataFound.getString(0);
//                            //TextView titleView = new TextView(getContext());
//                            events.add(title);
//                            //linearLayout.addView(titleView);
//                        } while (dataFound.moveToNext());
//                    }
//                }
//                listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.event_child_layout, R.id.eventTitleTextView, events));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private View findViewById(int id){
        return rootView.findViewById(id);
    }
}
