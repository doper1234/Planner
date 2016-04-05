package com.example.chris.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.text.Layout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Chris on 23/03/2016.
 */
public class BluetoothConnection {


    BluetoothActivity activity;
    private BluetoothAdapter BA;
    private BluetoothDevice deviceToConnectTo;
    private Set<BluetoothDevice>pairedDevices;
    private LinearLayout ll;
    private String lookingForAddress = "";
    private UUID uuidToUse;
    private BluetoothServer bluetoothServer;
    public BluetoothConnection(BluetoothActivity a, LinearLayout ll){
        activity = a;
        BA = BluetoothAdapter.getDefaultAdapter();
        TextView bluetoothDevice = new TextView(activity);
        bluetoothDevice.setText("Your bluetooth address: " + BA.getAddress() + ". Enter this in your partner's device and press connect.");
        ll.addView(bluetoothDevice);
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(turnOn, 0);
            Toast.makeText(activity.getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_LONG).show();
            if (BA.getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Log.d("Blue", "changing scan mode");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                a.startActivity(discoverableIntent);
            }
        } else{
            Toast.makeText(activity.getApplicationContext(),"Bluetooth already on", Toast.LENGTH_LONG).show();
        }
    }
    public void startBluetoothSearch(LinearLayout layout, String lookingForAddress){


        this.lookingForAddress = lookingForAddress;
        ll = layout;
        deviceToConnectTo = BA.getRemoteDevice(lookingForAddress);
        //if(deviceToConnectTo.getName() !=null){
            TextView bluetoothDeviceView = new TextView(activity);
            bluetoothDeviceView.setText(deviceToConnectTo.getName() + " with address of " + deviceToConnectTo.getAddress() + " found already paired. Click transfer to transfer data!");
            layout.addView(bluetoothDeviceView);

        //}else{
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(mReceiver, filter);

// Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            activity.registerReceiver(mReceiver, filter);


            filter = new IntentFilter( BluetoothAdapter.ACTION_DISCOVERY_STARTED );
            activity.registerReceiver( mReceiver, filter );

        //}

//
//// Get the local Bluetooth adapter
        //BA = BluetoothAdapter.getDefaultAdapter();

//
//
////        if ( !BA.isEnabled())
////        {
////            Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
////            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT );
////        }
//        if (!BA.isEnabled()) {
//            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            activity.startActivityForResult(turnOn, 0);
//            Toast.makeText(activity.getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_LONG).show();
//        } else
//        {
//            Toast.makeText(activity.getApplicationContext(),"Bluetooth already on", Toast.LENGTH_LONG).show();
//        }
        pairedDevices = BA.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices){
            TextView bluetoothDevice = new TextView(activity);
            bluetoothDevice.setText(bt.getName() + " address: " + bt.getAddress());
            List<String> uuids = new ArrayList<>();
            for(ParcelUuid uuid : bt.getUuids()){
                uuids.add(uuid.toString());
            }
            activity.initializeExpandableListView(bt.getName(), bt.getAddress(), "Paired", uuids);
            layout.addView(bluetoothDevice);
            Log.d(bt.getName(), bt.getAddress());
        }

        BA.startDiscovery();
    }

    public void stopBluetoothSearch(){
        BA.disable();
        Toast.makeText(activity.getApplicationContext(),"Bluetooth turned off" ,Toast.LENGTH_LONG).show();
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }

    public void getDiscoverableBluetoothDevices(){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        activity.startActivityForResult(getVisible, 0);
    }

    public void connectToDevice(String bluetoothAddress){
        this.lookingForAddress = bluetoothAddress;
        deviceToConnectTo = BA.getRemoteDevice(lookingForAddress);
        if(deviceToConnectTo != null){
            Toast.makeText(activity, "Connected to " + deviceToConnectTo.getName(), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(activity, "Error connecting to device", Toast.LENGTH_LONG).show();
        }

    }

    public void sendEvents(){
//        String TAG = "BluetoothDebug";
//        final UUID MY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
//        BluetoothDevice device = deviceToConnectTo;
//        if(device.getBondState()==device.BOND_BONDED) {
//            Log.d(TAG, device.getName());
//            BluetoothSocket mSocket = null;
//            try {
//                mSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                Log.d(TAG, "socket not created");
//                e1.printStackTrace();
//            }
//            try {
//                mSocket.connect();
//            } catch (IOException e) {
//                try {
//                    mSocket.close();
//                    Log.d(TAG, "Cannot connect");
//                } catch (IOException e1) {
//                    Log.d(TAG, "Socket not closed");
//                    e1.printStackTrace();
//                }
//            }
//        }
//        ParcelUuid[] deviceUUIDS = deviceToConnectTo.getUuids();
////        for (int i = 0; i < deviceUUIDS.length; i++) {
////            TextView t = new TextView(activity);
////            t.setText(deviceUUIDS[i].toString());
////            ll.addView(t);
////        }
//        uuidToUse = UUID.fromString(deviceUUIDS[deviceUUIDS.length-1].toString());
//        BluetoothServer bluetoothServer = new BluetoothServer(activity,ll, uuidToUse);
//        //bluetoothServer.start(bluetoothServer.run());
//        bluetoothServer.run();

        bluetoothServer = new BluetoothServer(BA, null, true, activity, ll);

    }

    public void receiveConnection(String bluetoothAddress){
        bluetoothServer = new BluetoothServer(BA, bluetoothAddress, false, activity, ll);

//        sendEvents();
//        BluetoothDevice bt = BA.getRemoteDevice(bluetoothAddress);
//        BluetoothSocket bluetoothSocket;
//        try {
//            bluetoothSocket = bt.createRfcommSocketToServiceRecord(uuidToUse);
//            bluetoothSocket.connect();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void sendMessage(String message){
        bluetoothServer.sendMessage(message);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

//            TextView t = new TextView(activity);
//            t.setText("Starting search...");
//            ll.addView(t);
            // When discovery finds a device

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //do something
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                List<String> uuids = new ArrayList<>();
                String pairedStatus;
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    pairedStatus = "paired";
                }else{
                    pairedStatus = "not paired";
                }
                if(device.getUuids() != null){
                    for(ParcelUuid uuid : device.getUuids()){
                        uuids.add(uuid.toString());
                    }
                }else{
                    uuids.add("no valid uuids");
                }

                activity.initializeExpandableListView(device.getName(), device.getAddress(), pairedStatus, uuids);
                TextView t1 = new TextView(activity);
                TextView t2 = new TextView(activity);
                if(device.getAddress().equalsIgnoreCase(lookingForAddress)){
                    t1.setText("Found your device! Wait to complete pairing...");
                    device.createBond();
                    deviceToConnectTo = device;
                    BA.cancelDiscovery();
//                    BluetoothDevice bd = BA.getRemoteDevice(lookingForAddress);
                    //t2.setText("Uuids " +  bd.getUuids().toString());
                }else{
                    t1.setText("Found " + device.getName() + " with address of " + device.getAddress());
                }
                ll.addView(t1);
                ll.addView(t2);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                TextView t2 = new TextView(activity);
//                t.setText("Didn't find a device. Restarting search.");
//                ll.addView(t2);
//                Log.d("Done searching", "Entered the Finished ");
                BA.startDiscovery();
            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                TextView t2 = new TextView(activity);
                t2.setText("Device paired! Click transfer events to transfer data.");
                ll.addView(t2);
            }
        }
    };

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            try
//            {
//                String action = intent.getAction();
//                TextView t = new TextView(activity);
//                t.setText("still searching?");
//                ll.addView(t);
//                // When discovery finds a device
//                if ( BluetoothDevice.ACTION_FOUND.equals(action) )
//                {
//                    // Get the BluetoothDevice object from the Intent
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                    String deviceName = device.getName();
//                    String deviceAddress = device.getAddress();
//
//                    Toast.makeText(activity.getApplicationContext(),"Bluetooth turned off" ,Toast.LENGTH_LONG).show();
//                    Log.d ("DEVICE FOUND", deviceName + " Address : " + deviceAddress );
//                }
//            }
//            catch ( Exception e )
//            {
//                System.out.println ( "Broadcast Error : " + e.toString() );
//            }
//        }
//    };

}
