package com.example.chris.planner;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends Activity {

    final String divider = "1N2E3W45";
    private String bluetoothAddress = "44:D8:84:11:90:9D";
    private BluetoothConnection bc;
    private String[] bluetoothDevicesNames = {
            "Chris's iPhone",
            "Anna Owsiak Galaxy S Pro",
            "Piece O' Shit Honda",
            "Petermobile"
    };
    private String[] bluetoothAddresses = {
            "38:D4:0B:3B:69:5F",
            "10:30:47:DC:64:D0",
            "01:45:4R:DF:60:G8",
            "11:22:33:AA:BB:44"

    };

    private String[] bluetoothIsPaired = {
            "yes", "no", "no", "no"
    };
    private String[] bluetoothUUIDs = {
            "68680aae-601e-4f82-af2a-eb870b0c0e01",
            "f87c26f2-158a-4f89-9805-564fc5fdf4f7",
            "f87c26f2-158a-4f89-9805-564fc5fdf4f7",
            "f87c26f2-158a-4f89-9805-564fc5fdf4f7"
    };
    List<String> deviceName;
    List<String> deviceAddress;
    List<String> deviceIsPaired;
    List<String> deviceUUIDs;
    HashMap<String, List<String>> childList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        //setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bluetoothDevicesNames));
        setupBluetoothScreen();
        //setColours();
    }
    private void setupBluetoothScreen(){
        final Button backButton, searchButton, connectButton, transferButton, receiveButton, testSendButton, loadButton;
        final EditText addressText = (EditText) findViewById(R.id.editTextBluetoothAddress);
        deviceName = new ArrayList<>();
        deviceAddress = new ArrayList<>();
        deviceIsPaired = new ArrayList<>();
        deviceUUIDs = new ArrayList<>();
        childList = new HashMap<String, List<String>>();

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
                searchButton.setText("Start search");
                LinearLayout ll = (LinearLayout) findViewById(R.id.bluetoothSearchLayout);
                Button connectButton = (Button) findViewById(R.id.connectToPartnerButton);
                bc = new BluetoothConnection(BluetoothActivity.this, ll);
                connectButton.setEnabled(true);
                bc.startBluetoothSearch(ll, "00:21:CC:11:29:4E");
                //initializeExpandableListView();
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

    public void initializeExpandableListView(String name, final String address, String isPaired, List<String> uuid){
        //check if device with the same bluetooth address is already in the expandable list view
        for(String addressExists: deviceAddress){
            if(address.equalsIgnoreCase(addressExists)){
                return;
            }
        }
        //add device and info
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LinearLayout ll = (LinearLayout)findViewById(R.id.displayEventsLayout);
                TextView eventsFoundView = (TextView) findViewById(R.id.eventsFoundView);
                eventsFoundView.setVisibility(View.VISIBLE);
                TextView t = new TextView(BluetoothActivity.this);
                if(childPosition == 0){
                    t.setText("Paired");
                }else if(childPosition == 1){
                    t.setText("Send Events");
                    bc.sendEvents();
                }else if(childPosition == 2){
                    t.setText("Receive Events");
                    bc.receiveConnection(deviceAddress.get(groupPosition));
                }else{
                    bc.connectToDevice(deviceAddress.get(groupPosition));
                    t.setText("Connect to this device");
                }
                ll.addView(t);

                return true;
            }
        });
        deviceName.add(name);
        deviceAddress.add(address);
        deviceIsPaired.add(isPaired);
        deviceUUIDs = uuid;
//        List<String> deviceName = new ArrayList<>();
//        List<String> deviceAddress = new ArrayList<>();
//        List<String> deviceIsPaired = new ArrayList<>();
//        List<String> deviceUUIDs = new ArrayList<>();
//        HashMap<String, List<String>> childList = new HashMap<String, List<String>>();
//        for(String title : bluetoothDevicesNames){
//            deviceName.add(title);
//        }
//        for(String address : bluetoothAddresses){
//            deviceAddress.add(address);
//        }
//        for(String isPaired : bluetoothIsPaired){
//            deviceIsPaired.add(isPaired);
//        }
//        for(String uuid : bluetoothUUIDs){
//            deviceUUIDs.add(uuid);
//        }
        for (int i = 0; i < deviceName.size(); i ++){
            List<String> deviceInfo = new ArrayList<>();
            //deviceInfo.add(deviceName.get(i));
            //deviceInfo.add(deviceAddress.get(i));
            deviceInfo.add(deviceIsPaired.get(i));
            //deviceInfo.add(deviceUUIDs.get(i));
            deviceInfo.add("Send Events");
            deviceInfo.add("Receive Events");
            deviceInfo.add("Connect to this device");
            childList.put(deviceName.get(i), deviceInfo);
        }
        BluetoothExpandableListAdapter adapter = new BluetoothExpandableListAdapter(this, deviceName, childList);
        expandableListView.setAdapter(adapter);
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
