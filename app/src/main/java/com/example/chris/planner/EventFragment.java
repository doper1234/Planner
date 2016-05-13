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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 21/04/2016.
 */
public class EventFragment extends Fragment {

    private Context ctx;
    private Activity activity;
    private DatabaseEventListAdapter dataBaseEventListAdapter;
    private TodaysEventsFragment todaysEventsFragment;

    public EventFragment(){
        ctx = getContext();
    }

    public EventFragment(Context c, Activity a, TodaysEventsFragment tef){
        ctx = c;
        activity = a;
        todaysEventsFragment = tef;

    }
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.event_fragment_layout, container, false);
        loadEvents();
        setupSearch();
        return rootView;

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
                String eventName, eventFrequency;
                String eventDuration;
                eventName = cr.getString(0);
                eventFrequency = cr.getString(1);
                eventDuration = (cr.getString(2));

                events.add(eventName);
                frequencies.add(eventFrequency);
                durations.add(eventDuration);
            } while (cr.moveToNext());
        }else{
            events.add("You have no events. Why don't you add some?");
        }
    dataBaseEventListAdapter = new DatabaseEventListAdapter(getActivity(), this, todaysEventsFragment, events, frequencies, durations);
    listView.setAdapter(dataBaseEventListAdapter);
    }

    //
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
                    listView.setAdapter(new DatabaseEventListAdapter(getActivity(), EventFragment.this, todaysEventsFragment, events, frequencies, durations));
                } else {
                    loadEvents();
                }

                return true;
            }
        });
    }

    public void removeElement(String removeElement){
        dataBaseEventListAdapter.remove(removeElement);

    }

    //helper method for use without rootView.
    private View findViewById(int id){
        return rootView.findViewById(id);
    }
}
