package com.example.pavan.callrecorderdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    TextView textView;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> records=new ArrayList<>();
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //write something after giving permissions
        }else {
            checkPermissions();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.records_list);
        textView = findViewById(R.id.noRecordText);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);

        listView.setAdapter(arrayAdapter);

        if(records.size()>0){
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        checkPermissions();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
                askPermission();
        }
    }



    public void checkPermissions(){

        ArrayList<String> permissions=new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.CALL_PHONE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_CALL_LOG);
        }

        if(permissions.size() >0)
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[0]),101);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askPermission() {
        Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
        startActivityForResult(intent,ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            if(!Settings.canDrawOverlays(this)){
                askPermission();
            }
        }
    }
}
