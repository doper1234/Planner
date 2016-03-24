package com.example.chris.planner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothConnection bc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        setupBluetoothScreen();
        setColours();
    }
    private void setupBluetoothScreen(){
        Button backButton, searchButton, connectButton, transferButton;
        final EditText addressText = (EditText) findViewById(R.id.editTextBluetoothAddress);
        connectButton = (Button) findViewById(R.id.connectToPartnerButton);
        connectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bc.connectToDevice(addressText.getText().toString());
            }
        });
        transferButton = (Button) findViewById(R.id.bluetoothTransferButton);
        //transferButton.setEnabled(false);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bc.sendEvents();
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
                Button connectButton = (Button) findViewById(R.id.connectToPartnerButton);
                LinearLayout ll = (LinearLayout) findViewById(R.id.bluetoothSearchLayout);
                bc = new BluetoothConnection(BluetoothActivity.this);
                bc.startBluetoothSearch(ll, "44:D8:84:11:90:9D");
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


}
