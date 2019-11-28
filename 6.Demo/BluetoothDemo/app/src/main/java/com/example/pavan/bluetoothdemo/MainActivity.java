package com.example.pavan.bluetoothdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    TextView statusText;
    ProgressBar progressBar;
    ListView listView;
    Button button;
    IntentFilter intentFilter;
    Switch aSwitch;
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> bluetoothContent = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            registerReceiver(broadcastReceiver,intentFilter);
            findDevices(null);
        }
    }

    public BluetoothAdapter bluetoothAdapter ;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            Log.i("ACtion ",action);

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String address= device.getAddress();
                String name=device.getName();
                String rssi = String.valueOf(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));

                if(!addresses.contains(address)){
                    addresses.add(address);
                    String addString;
                    if(name == null || name.equals("")){
                        addString= address + " - RSSI "+rssi+"-dBm";
                    }else{
                        addString = name + " - RSSI "+rssi+"-dBm";
                    }
                    Log.i("addStr",addString);
                    bluetoothContent.add(addString);
                    arrayAdapter.notifyDataSetChanged();
                }

            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                statusText.setText(R.string.finishText);
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusText = findViewById(R.id.statusText);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.bluetoothDevicesList);
        button=findViewById(R.id.searchButton);
        aSwitch = findViewById(R.id.switch1);

        if(bluetoothAdapter!=null){
            aSwitch.setChecked(true);
            bluetoothAdapter.startDiscovery();
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,bluetoothContent);
        listView.setAdapter(arrayAdapter);

        intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},101);
        }

        bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();

        registerReceiver(broadcastReceiver,intentFilter);

       aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   bluetoothAdapter.enable();
               }else{
                   bluetoothAdapter.disable();
               }
           }
       });

    }

    public void findDevices(View view) {
        statusText.setText(R.string.searchText);
        progressBar.setVisibility(View.VISIBLE);
        bluetoothContent.clear();
        arrayAdapter.notifyDataSetChanged();
        button.setEnabled(false);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
