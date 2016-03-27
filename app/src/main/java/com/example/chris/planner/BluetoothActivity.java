package com.example.chris.planner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    final String divider = "1N2E3W45";
    private String bluetoothAddress = "44:D8:84:11:90:9D";
    private BluetoothConnection bc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        setupBluetoothScreen();
        setColours();
    }
    private void setupBluetoothScreen(){
        final Button backButton, searchButton, connectButton, transferButton, receiveButton, testSendButton, loadButton;
        final EditText addressText = (EditText) findViewById(R.id.editTextBluetoothAddress);
        loadButton = (Button) findViewById(R.id.loadButton);
        connectButton = (Button) findViewById(R.id.connectToPartnerButton);
        transferButton = (Button) findViewById(R.id.bluetoothTransferButton);
        receiveButton = (Button) findViewById(R.id.receiveEventsButton);
        //testSendButton = (Button) findViewById(R.id.testSendButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase();
            }
        });
//        testSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getAndSendDatabase();
//            }
//        });
        connectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bc.connectToDevice(addressText.getText().toString());
                bluetoothAddress = addressText.getText().toString();
                LinearLayout ll = (LinearLayout) findViewById(R.id.bluetoothSearchLayout);
                bc.startBluetoothSearch(ll, bluetoothAddress);
                connectButton.setEnabled(true);
                transferButton.setEnabled(true);
                receiveButton.setEnabled(true);
            }
        });
        transferButton.setEnabled(false);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveButton.setEnabled(false);
                bc.sendEvents();
                getAndSendDatabase();

            }
        });
        receiveButton.setEnabled(false);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferButton.setEnabled(false);
                bluetoothAddress = addressText.getText().toString();
                bc.receiveConnection(bluetoothAddress);

            }
        });

        backButton = (Button) findViewById(R.id.bluetoothBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bc != null){
                    bc.stopBluetoothSearch();
                }else{
                    startActivity(new Intent(BluetoothActivity.this, SettingsActivity.class));
                }
            }

        });
        searchButton = (Button) findViewById(R.id.bluetoothSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.bluetoothSearchLayout);
                Button connectButton = (Button) findViewById(R.id.connectToPartnerButton);
                bc = new BluetoothConnection(BluetoothActivity.this, ll);
                connectButton.setEnabled(true);
            }
        });

    }
    private void setColours(){
        findViewById(R.id.bluetoothScrollView).setBackgroundColor(Colours.foregroundColour);
        findViewById(R.id.bluetoothBackButton).setBackgroundColor(Colours.buttonColour);
        findViewById(R.id.bluetoothSearchButton).setBackgroundColor(Colours.buttonColour);
        findViewById(R.id.bluetoothMainLayout).setBackgroundColor(Colours.backgroundColour);
        findViewById(R.id.bluetoothSearchLayout).setBackgroundColor(Colours.foregroundColour);
    }

    private void updateDatabase(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.bluetoothSearchLayout);
        if(TableData.TableInfo.READY_TO_LOAD){
            TableData.TableInfo.READY_TO_LOAD = false;
            String newTable = TableData.TableInfo.INCOMING_TABLE;

//            TextView t = new TextView(this);
//            t.setText(newTable);
//            ll.addView(t);
            TableData.TableInfo.INCOMING_TABLE = "";
            String[] events = newTable.split(divider);
            DataBaseOperations dbo = new DataBaseOperations(this);
            for (int i = 0; i < events.length; i++) {
                String[] eventInfo = events[i].split("/");
                TextView t1 = new TextView(this);
                //t1.setText("Events saved!");
                  if(i != 0){
                    t1.setText("New event: " + eventInfo[1] + " Frequency: " + eventInfo[2] + " Duration: " + eventInfo[4]);
                    dbo.putInformation(this, dbo, eventInfo[1], eventInfo[2], Integer.parseInt(eventInfo[4]));
                }
                ll.addView(t1);
            }
        }else{
            TextView t = new TextView(this);
            t.setText("No data to download.");
            ll.addView(t);
        }
    }

    private void getAndSendDatabase(){
        DataBaseOperations dbo = new DataBaseOperations(BluetoothActivity.this);
        Cursor cr = dbo.getInformation(dbo);
        String database = "";
        if(cr.moveToFirst()){
            do{
                String eventName, eventFrequency, eventFinished, eventDuration, eventInitialDuration;
                eventName = cr.getString(0);
                eventFrequency = cr.getString(1);
                eventDuration = cr.getString(2);
                eventInitialDuration = cr.getString(3);
                eventFinished = "no";

                database = database + " "+divider+"/" + eventName + "/" + eventFrequency +"/" + eventFinished + "/" + eventDuration +"/" + eventInitialDuration;
            }while(cr.moveToNext());
        }
        bc.sendMessage(database);
    }


}
