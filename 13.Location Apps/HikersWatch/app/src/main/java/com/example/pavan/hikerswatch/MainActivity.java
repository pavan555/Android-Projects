package com.example.pavan.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView lat,lon,address,accuracy,alt;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = findViewById(R.id.Latitude);
        lon = findViewById(R.id.Longitude);
        alt = findViewById(R.id.Alt);
        address = findViewById(R.id.address);
        accuracy = findViewById(R.id.accuracy);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat.setText(String.valueOf(location.getLatitude()));
                lon.setText(String.valueOf(location.getLongitude()));
                alt.setText(String.valueOf(location.getAltitude()));
                accuracy.setText(String.valueOf(location.getAccuracy()));
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    Log.i("List",list.toString());
                    if(list.get(0)!=null && list.size() >0){
                        String add ="";
                        if(list.get(0).getThoroughfare() != null){
                            add+=list.get(0).getThoroughfare()+", ";
                        }

                        if(list.get(0).getLocality() != null){
                            add+=list.get(0).getLocality()+", ";
                        }
                        if(list.get(0).getPostalCode() != null){
                            add+=list.get(0).getPostalCode()+", ";
                        }
                       if(list.get(0).getSubAdminArea() != null){
                           add+=list.get(0).getSubAdminArea()+", ";
                       }
                       if(list.get(0).getAdminArea() != null){
                           add+=list.get(0).getAdminArea();
                       }

                       address.setText(add);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }




}
