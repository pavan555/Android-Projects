package com.example.pavan.locationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.location.LocationManager.*;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView tv;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);

         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    Log.i("LOc", location.toString());
                    tv.setText(String.valueOf(location));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                        tv.setText("Status Changed"+String.valueOf(status));
                }

                @Override
                public void onProviderEnabled(String provider) {
                        tv.setText("Provider Enabled"+provider.toString());
                }

                @Override
                public void onProviderDisabled(String provider) {
                    tv.setText("Provider Disabled"+provider.toString());

                }

            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            } else {
                locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0, 0, locationListener);
            }


    }
}
