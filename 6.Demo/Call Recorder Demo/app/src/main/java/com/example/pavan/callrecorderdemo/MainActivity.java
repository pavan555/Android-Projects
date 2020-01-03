package com.example.pavan.callrecorderdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    public static ArrayAdapter<String> arrayAdapter;
    public static ArrayList<String> records=new ArrayList<>();
    public static SQLiteDatabase sqLiteDatabase;
    public static int RECORDS_LENGTH=0;

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

        sqLiteDatabase=this.openOrCreateDatabase("records",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS RECORDS(ID INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,fileName VARCHAR,time VARCHAR,phoneNumber VARCHAR)");



        listView = findViewById(R.id.records_list);
        textView = findViewById(R.id.noRecordText);

        arrayAdapter = new ArrayAdapter<>(this,R.layout.simple_my_items_layout, records);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM RECORDS WHERE fileName="+DatabaseUtils.sqlEscapeString(records.get(position)),null);
                c.moveToFirst();
                Intent intent=new Intent(MainActivity.this,AudioEditActivity.class);
                intent.putExtra("ID",c.getString(0));
                intent.putExtra("position",position);
                intent.putExtra("fileName",c.getString(1));
                intent.putExtra("time",c.getString(2));
                intent.putExtra("phoneNumber",c.getString(3));
                c.close();
                startActivity(intent);
            }
        });




        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM RECORDS",null);
        cursor.moveToFirst();
        records.clear();
        while (!cursor.isAfterLast()){
            try{
                records.add(cursor.getString(1));
                RECORDS_LENGTH++;
                cursor.moveToNext();
            }catch (Exception exception){
                break;
            }
        }
        cursor.close();
        if(records.size()>0){
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
        arrayAdapter.notifyDataSetChanged();


        checkPermissions();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRecords();
            }
        },5000);

        this.runOnUiThread(

                new Runnable() {
                    @Override
                    public void run() {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)){
                            askPermission();
                        }

                    }
                });

    }


    public static void checkRecords(){
//        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recordedCalls";
//        File directory=new File(path);
//        File[] files=directory.listFiles();

        int count= (int) DatabaseUtils.queryNumEntries(sqLiteDatabase,"RECORDS");

        if(count > RECORDS_LENGTH){
            Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM RECORDS ORDER BY ID DESC LIMIT 1",null);
            c.moveToFirst();
            if(!c.isAfterLast()){
                records.add(c.getString(1));
                arrayAdapter.notifyDataSetChanged();
                RECORDS_LENGTH++;
            }
            c.close();
        }

//        try{
//            for(int i=0;i<files.length;i++){
//                Log.i("MAIN",files[i].getName());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }


    /*
        Checking required permissions
     */


    public void checkPermissions(){

        ArrayList<String> permissions=new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.CALL_PHONE);
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        }


        if(permissions.size() >0)
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[0]),101);
    }



    /*
    Permission for OVERLAY WINDOW
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askPermission() {
        Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
        startActivityForResult(intent,ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

     /*
        Permission  granted or not checking
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            if(!Settings.canDrawOverlays(this)){
                askPermission();
            }
        }
    }



    /*
    Menu Creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.exit){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    })
                    .setNegativeButton("No",null).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
