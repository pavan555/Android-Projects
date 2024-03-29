package com.example.pavan.favouriteplaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.sentry.Sentry;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayList<String> places = new ArrayList<>();
    ArrayList<String> latitudes=new ArrayList<>();
    ArrayList<String> longitudes=new ArrayList<>();
    static ArrayList<LatLng> locations=new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.pavan.favouriteplaces",MODE_PRIVATE);

        listView = findViewById(R.id.list);
        locations.clear();
        places.clear();
        longitudes.clear();
        latitudes.clear();

        try {
            places= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs",ObjectSerializer.serialize(new ArrayList<String>())));

            if(places.size()>0 && latitudes.size()>0 && longitudes.size()>0){

                if(places.size()==latitudes.size() && places.size() == longitudes.size()){
                    for(int i=0;i<latitudes.size();i++){
                        locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
                    }


                }

            }else{
                places.add("Add New Place ....");
                locations.add(new LatLng(0,0));
            }


        } catch (Exception e) {
            e.printStackTrace();
            Sentry.capture(e);
        }

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,places);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });

    }
}
