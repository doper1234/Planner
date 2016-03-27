//package com.example.chris.planner;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * Created by Chris on 24/03/2016.
// */
//public class BluetoothServer extends Thread{
//
//    private Activity a;
//    private LinearLayout ll;
//
//    private final BluetoothServerSocket mmSocketServer; //
//
//    public BluetoothServer(Activity a, LinearLayout ll, UUID uuid) {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        BluetoothServerSocket temp = null;
//
//        this.a = a;
//        this.ll = ll;
//        TextView t = new TextView(a);
//        try {
//            temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("", uuid);
//            t.setText("Got uuid " + uuid.toString());
//
//        } catch (IOException e) {
//            t.setText("Unsuccessful connection.");
//            e.printStackTrace();
//        }
//        mmSocketServer = temp;
//        ll.addView(t);
//    }
//    public void run(){
//        BluetoothSocket socket = null;
//        while(true){
//            TextView t = new TextView(a);
//            try{
//                t.setText("Connection to partner successful.");
//                socket = mmSocketServer.accept();
//                ll.addView(t);
//
//            }
//            catch(IOException e){
//                t.setText("Couldn't connect to partner's device. Press send to try again.");
//                ll.addView(t);
//                break;
//            }
//            if(socket != null){
//                try{
//                    mmSocketServer.close();
//                }
//                catch(IOException e){
//                    break;
//                }
//            }
//
//        }
//    }
//}
package com.example.chris.planner;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class BluetoothServer extends Thread implements Runnable{
    final static String TAG = "DEBUG";
    private final BluetoothServerSocket mmServerSocket;
    final static UUID MY_UUID = UUID.fromString("8aa46e71-dabc-4f38-8a9c-455d31ae0ff7");
    final static String NAME = "something_generic";
    private BluetoothAdapter mBluetoothAdapter;
    public ConnectedThread ct;
    public Activity ac;
    public LinearLayout ll;

    public BluetoothServer(BluetoothAdapter BA, String address, boolean server, Activity a, LinearLayout ll) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        ac = a;
        this.ll = ll;
        if(server){
            BluetoothServerSocket tmp = null;
            this.mBluetoothAdapter = BA;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                Log.d(TAG, "Starting server");
            } catch (IOException e) {
                Log.d(TAG, "Error starting server");
            }
            mmServerSocket = tmp;
            run();
        }else{
            Log.d(TAG, "connect thread from remote device with address " + address);
            mmServerSocket = null;
            Log.d(TAG, "Cancelling server");
            cancel();
            ConnectThread ct = new ConnectThread(BA.getRemoteDevice(address));
            ct.run();
        }

    }


    public void run() {
        BluetoothSocket socket = null;
        Log.d(TAG, "Starting wait for partner...");
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                Log.d(TAG, "Waiting for partner...");
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)

                ct = new ConnectedThread(socket);
                //ct.run();
                try {
                    mmServerSocket.close();
                    Log.d("HEY", "Server socket closed.");
                    cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("HEY", "couldn't close socket.");
                }
                break;
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (Exception e) { }
    }

    private void manageConnectedSocket(BluetoothSocket socket){

    }

    public void sendMessage(String message){
        ct.write(message.getBytes());
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
            Log.d(TAG, "Trying to initialize socket " + tmp);
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
//            mBluetoothAdapter.cancelDiscovery();

            TextView t = new TextView(ac);
            t.setText("Connecting...");
            ll.addView(t);
            try {
                Log.d(TAG, "Trying to connect to socket");
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.d(TAG, "Unable to connect.");
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                TextView t1 = new TextView(ac);
                t1.setText("Failed.");
                ll.addView(t1);
                return;
            }

            // Do work to manage the connection (in a separate thread)
            ct = new ConnectedThread(mmSocket);
            //ct.run();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread implements Runnable{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            //write("hi".getBytes());
            TextView t = new TextView(ac);
            t.setText("Connected!");
            ll.addView(t);
            Log.d(TAG, "Connected!");
            start();
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            Log.d("incoming message", "running reader!!!");
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String dataReceived = new String(buffer, "UTF-8");
                    Log.d(TAG + "RECEIVED", dataReceived);
                    TableData.TableInfo.INCOMING_TABLE = dataReceived;
                    //mHandler.obtainMessage("idk what goes here honey", bytes, -1, buffer)
                     //       .sendToTarget();

//                    TextView t = new TextView(ac);
//                    t.setText("Bytes receieved: " + bytes);
//                    ll.addView(t);
                    Log.d("incoming message", bytes + " bytes");
                    TableData.TableInfo.READY_TO_LOAD = true;
                    break;
                    // Send the obtained bytes to the UI activity
//                    Handler mHandler = new Handler() {
//                        @Override
//                        public void close() {
//
//                        }
//
//                        @Override
//                        public void flush() {
//
//                        }
//
//                        @Override
//                        public void publish(LogRecord record) {
//
//                        }
//                    };
//                            mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
            Log.d(TAG, "on the outside");
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                TextView t = new TextView(ac);
                t.setText("Bytes sent: " + new String(bytes));
                ll.addView(t);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public Handler mHandler = new Handler() {

        public void obtainMessage(String message, int bytes, int val, byte[] buffer){

        }

        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord record) {

        }
    };
}