package com.example.chris.planner;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 22/04/2016.
 */
public class SyncWithServerFragment   extends Fragment {

    ViewGroup rootView;
    private String name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.sync_with_server_layout, container, false);
        setupGUI();
        return rootView;
    }

    private void setupGUI(){
        List<String> d = new ArrayList<>();
        d.add("Do it");
        d.add("loud");
        d.add("and do it");
        d.add("proud");
        d.add("killjoys");
        d.add("make some noise!");
        setupEventsList(d);
        //set checkLogin button listener; checkLogin on touch
        rootView.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName =  ((EditText)rootView.findViewById(R.id.accountNameEditText)).getText().toString();
                String accountPassword =  ((EditText)rootView.findViewById(R.id.accountPasswordEditText)).getText().toString();
                checkLogin(accountName, accountPassword);
            }
        });
        // set create new account listener; create new account on touch
        rootView.findViewById(R.id.newAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = ((EditText) rootView.findViewById(R.id.accountNameEditText)).getText().toString();
                String accountPassword = ((EditText) rootView.findViewById(R.id.accountPasswordEditText)).getText().toString();
                String confirmAccountPassword = ((EditText) rootView.findViewById(R.id.confirmAccountPasswordEditText)).getText().toString();
                if (accountPassword.equals(confirmAccountPassword)) {
                    createAccount(accountName, accountPassword);
                } else {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT);
                }

            }
        });

        rootView.findViewById(R.id.createNewAccountTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.confirmAccountPasswordLinearLayout).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.newAccountButton).setEnabled(true);
                rootView.findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
            }
        });

        rootView.findViewById(R.id.syncButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = ((EditText) rootView.findViewById(R.id.accountNameEditText)).getText().toString();

                syncEvents(accountName);
            }
        });


    }

    private void checkLogin(String name, String password){
        this.name = name;
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations(this);
        new DataBaseOperations(getContext()).close();

        if(remoteDatabaseOperations.checkLogin(name, password)){
            rootView.findViewById(R.id.syncButton).setEnabled(true);
            Toast.makeText(getContext(), "You have been signed in!", Toast.LENGTH_SHORT).show();
            ((TextView)rootView.findViewById(R.id.loggedInAsTextView)).setText("Logged in as: " + name);

        }else{
            Toast.makeText(getContext(), "Account doesn't exists. Try again, it might need to be clicked twice", Toast.LENGTH_SHORT).show();
        }

    }

    public void login(boolean hasLoggedIn){
        if(hasLoggedIn){
            rootView.findViewById(R.id.syncButton).setEnabled(true);
            Toast.makeText(getContext(), "You have been signed in!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Account doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccount(String name, String password){
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations(this);
        //if(remoteDatabaseOperations.checkLogin(name, password)){
            remoteDatabaseOperations.createAccount(name, password);
//        }else{
//            Toast.makeText(getContext(), "Account already exists", Toast.LENGTH_SHORT);
//        }
    }

    private void syncEvents(String name){
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations(this);
        DataBaseOperations dataBaseOperations = new DataBaseOperations(getContext());
        Cursor events = dataBaseOperations.getInformation(dataBaseOperations);
        if(events.moveToFirst()){
            sendEvents(dataBaseOperations, remoteDatabaseOperations);
        }

        remoteDatabaseOperations = new RemoteDatabaseOperations(this);
        getEvents(name, remoteDatabaseOperations);

        //dataBaseOperations.syncEvents(remoteDatabaseOperations.getEvents(name));

    }

    private void getEvents(String name, RemoteDatabaseOperations remoteDatabaseOperations){
        remoteDatabaseOperations.getEvents(name);
    }

    private void sendEvents(DataBaseOperations dataBaseOperations, RemoteDatabaseOperations remoteDatabaseOperations){
        Cursor eventData = dataBaseOperations.getAllInformation(dataBaseOperations);
        List<String> events = new ArrayList<>();
        if(eventData.moveToFirst()){
            do{
                events.add(eventData.getString(0) + "/" + eventData.getString(1) + "/" + eventData.getString(2) + "/" +eventData.getString(3) + "/" + eventData.getString(4) + "/" + eventData.getString(5) );
            }while(eventData.moveToNext());
        }

        remoteDatabaseOperations.sendEvents(name, events);
    }

    private void setupEventsList(List<String> events){
        ListView eventListView = (ListView) rootView.findViewById(R.id.syncEventsListView);
        eventListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.database_event_layout, R.id.databaseEventTextView, events));
    }
}
