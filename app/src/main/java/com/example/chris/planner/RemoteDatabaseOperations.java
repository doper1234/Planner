package com.example.chris.planner;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 22/04/2016.
 */
public class RemoteDatabaseOperations {

    final String databaseIP = "192.168.1.107";
    Socket databaseSocket;
    BufferedReader reader;
    PrintWriter writer;
    Thread readerThread;
    public RemoteDatabaseOperations(){

    }

    public boolean checkLogin(String name, String password){
        establishConnectionToDatabase(name, password);
        return false;
    }

    public void createAccount(String name, String password){

    }

    public List<String> getEvents(String name){

        return new ArrayList<>();
    }

    private void establishConnectionToDatabase(String name, String password){
        try {
            databaseSocket = new Socket(databaseIP, 3074);
            reader = new BufferedReader(new InputStreamReader(databaseSocket.getInputStream()));
            writer = new PrintWriter(databaseSocket.getOutputStream());
            writer.println("check," + name + "," + password);
            writer.flush();
            readerThread = new Thread(new IncomingReader());
            readerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection(){
        try {
            databaseSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String message;
            if(reader!= null) {
                while(true) {

                    try {
                            while ((message = reader.readLine()) != null) {
                                String[] result = message.split(" ");
                                Log.d("From server: ", message);
                            }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }
}
