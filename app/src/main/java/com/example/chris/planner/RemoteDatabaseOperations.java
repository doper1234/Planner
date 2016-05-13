package com.example.chris.planner;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 22/04/2016.
 */
public class RemoteDatabaseOperations {

    static boolean doesLoginExist = false;
    final static String databaseIP = "85.159.209.9";
    private List<String> events;
    final static int iPPort = 4004;
    Socket databaseSocket;
    BufferedReader reader;
    PrintWriter writer;
    Thread readerThread;
    IncomingReader incomingReader;
    private String name, password;
    SyncWithServerFragment syncWithServerFragment;
    public RemoteDatabaseOperations(SyncWithServerFragment swsf){
        syncWithServerFragment = swsf;
    }

    public boolean checkLogin(String name, String password){
        establishConnectionToDatabase(name, password);
        return doesLoginExist;
    }

    public void createAccount(String name, String password){
        this.name = name;
        this.password = password;
        incomingReader = new IncomingReader("create new account");
        readerThread = new Thread(incomingReader);
        readerThread.start();
    }

    public void sendEvents(String name, List<String> events){
        this.name = name;
        incomingReader = new IncomingReader("add events", events);
        readerThread = new Thread(incomingReader);
        readerThread.start();
    }

    public List<String> getEvents(String name){

        this.name = name;
        events = new ArrayList<>();
        incomingReader = new IncomingReader("get events");
        readerThread = new Thread(incomingReader);
        readerThread.start();
        Log.d("Events from server", events.toString());
        return events;
    }

    private void establishConnectionToDatabase(String name, String password){
        this.name = name;
        this.password = password;
        incomingReader = new IncomingReader("check if login exists");
        readerThread = new Thread(incomingReader);
        readerThread.start();

    }
    private void closeConnection(){
        try {
            databaseSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public class IncomingReader implements Runnable {
        boolean exists = false;
        String operation;
        List<String> events;
        public IncomingReader(String operation){
            this.operation = operation;
        }
        public IncomingReader(String operation, List<String> events){
            this.operation = operation;
            this.events = events;
        }
        @Override
        public void run() {
            String message;
            try {
                databaseSocket = new Socket(databaseIP, iPPort);
                reader = new BufferedReader(new InputStreamReader(databaseSocket.getInputStream()));
                writer = new PrintWriter(databaseSocket.getOutputStream());
                if(operation.contains("add events")){
                    writer.println(operation + "," + name + "," + events);
                }else if(operation.contains("get events")){
                    writer.println(operation + "," + name);
                }
                else{
                    writer.println(operation + "," + name + "," + password);
                }
                writer.flush();
//                readerThread = new Thread(new IncomingReader());
//                readerThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
                while(true) {
                    try {
                            while ((message = reader.readLine()) != null) {
                                if(Boolean.parseBoolean(message)){
                                    exists = true;
                                    doesLoginExist = true;
                                    //syncWithServerFragment.login(true);
                                }else if(message.contains("get events")){

                                    decodeEvents(message);
                                }
                                Log.d("From server: ", message);
//                                databaseSocket.close();
//                                break;
                                //return;
                            }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        }

        public boolean exists(){
            return exists;
        }

    }

    public void decodeEvents(String message){
        String[] result = message.split(", ");
        for (int i = 0; i < result.length; i++) {
            Log.d("Decoding: ", "Event " + result[i]);
            String[] singleEvent = result[i].split("event ");
            List<String> event = new ArrayList<>();
            for (int j = 0; j < singleEvent.length; j++) {
                if(singleEvent[j].contains("[")){
                    singleEvent[j] = singleEvent[j].substring(1);
                }
                if(singleEvent[j].contains("null")){
                    singleEvent[j] = "0";
                }
                //Log.d("Decoding: ", "    EventInfo " + singleEvent[j]);
                //events.add(singleEvent[j]);
                events.add(singleEvent[j]);

            }

        }
        Log.d("Got events ", events.toString());
        addEventsToDatabase();
    }

    private void addEventsToDatabase(){
        DataBaseOperations dbo = new DataBaseOperations(syncWithServerFragment.getContext());
        for (int i = 2; i < events.size(); i++) {
            System.out.println("Current event " + events.get(i));
            String[] eventInfo = events.get(i).split("/");
            String title, frequency;
            int duration;
            title = eventInfo[0];
            frequency = eventInfo[1];
            duration = Integer.parseInt(eventInfo[2]);
            dbo.putInformation(dbo, title, frequency, duration);
        }
        Log.d("ALL EVENTS LOADED", "Right?");

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("CONNECTION", response);
            //textResponse.setText(response);
            super.onPostExecute(result);
        }

    }
}
