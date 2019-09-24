package com.example.pavan.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> stringArrayList = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.pavan.notes", MODE_PRIVATE);
        ListView listView = findViewById(R.id.listView);

        try {
            stringArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notess", ObjectSerializer.serialize(new ArrayList<String>())));
            Log.i("string array", String.format("%s", stringArrayList));
            if (stringArrayList.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, addNotes.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        arrayAdapter = new ArrayAdapter(this, R.layout.simple_layout_mylist_items, stringArrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, addNotes.class);
                intent.putExtra("notes", stringArrayList.get(position));
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        onDestroying();
    }

    protected void onDestroying() {

        new AlertDialog.Builder(this)
                .setTitle("Do you really want to Exit!?")
                .setPositiveButton("Yes :(", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            getSharedPreferences("com.example.pavan.notes", MODE_PRIVATE).edit().putString("notess", ObjectSerializer.serialize(stringArrayList)).apply();
                            //Log.i("STRING ARRAY",String.format("%s",stringArrayList));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                })
                .setNegativeButton("Noo :)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, addNotes.class);
                startActivity(intent);
                return true;

            case R.id.exit:
                onDestroying();
                return true;

            default:
                return false;
        }

    }
}
