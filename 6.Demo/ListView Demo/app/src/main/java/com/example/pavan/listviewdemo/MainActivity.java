package com.example.pavan.listviewdemo;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.myListView);

        final List<String> names = new ArrayList<String>();
        names.add("Pavan");
        names.add("Sarath");
        names.add("Veera Naga Surya");
        names.add("Vijay");
        names.add("Sandeep");
        names.add("kumar");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, names);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialogue(names.get(position));
            }
        });

    }

    public void showAlertDialogue(String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Hi "+message);
        alertDialog.setCancelable(true);
        

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}
