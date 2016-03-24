package com.example.chris.planner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Chris on 24/03/2016.
 */
public class BluetoothServer extends Thread{

    private Activity a;
    private LinearLayout ll;

    private final BluetoothServerSocket mmSocketServer; // socket przy użyciu którego będzie odbywała się komunikacja.

    public BluetoothServer(Activity a, LinearLayout ll, UUID uuid) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket temp = null;

        this.a = a;
        this.ll = ll;
        TextView t = new TextView(a);
        try {
            temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Powitanie", uuid); // przypisujemy nazwe usługi i indetyfikator
            t.setText("Successful connection using " + uuid.toString());

        } catch (IOException e) {
            t.setText("Unsuccessful connection.");
            e.printStackTrace();
        }
        mmSocketServer = temp;
        ll.addView(t);
    }
    public void run(){
        BluetoothSocket socket = null;
        while(true){
            TextView t = new TextView(a);
            try{
                socket = mmSocketServer.accept();
                t.setText("socket accepted");
            }
            catch(IOException e){
                t.setText("socket not accepted?");
                break;
            }
            if(socket != null){
                try{
                    mmSocketServer.close();
                }
                catch(IOException e){
                    break;
                }
            }
            ll.addView(t);
        }
    }
}
