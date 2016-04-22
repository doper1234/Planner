package com.example.chris.planner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
        //set login button listener; login on touch
        rootView.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName =  ((EditText)rootView.findViewById(R.id.accountNameEditText)).getText().toString();
                String accountPassword =  ((EditText)rootView.findViewById(R.id.accountPasswordEditText)).getText().toString();
                login(accountName, accountPassword);
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
    }

    private void login(String name, String password){
        this.name = name;
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations();
        if(remoteDatabaseOperations.checkLogin(name, password)){
            rootView.findViewById(R.id.syncButton).setEnabled(true);
        }

    }

    private void createAccount(String name, String password){
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations();
        if(remoteDatabaseOperations.checkLogin(name, password)){
            remoteDatabaseOperations.createAccount(name, password);
        }else{
            Toast.makeText(getContext(), "Account already exists", Toast.LENGTH_SHORT);
        }
    }

    private void syncEvents(){
        RemoteDatabaseOperations remoteDatabaseOperations = new RemoteDatabaseOperations();
        DataBaseOperations dataBaseOperations = new DataBaseOperations(getContext());
        dataBaseOperations.syncEvents(remoteDatabaseOperations.getEvents(name));
    }
}
