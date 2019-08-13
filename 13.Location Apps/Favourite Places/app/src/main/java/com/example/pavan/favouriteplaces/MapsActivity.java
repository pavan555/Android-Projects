package com.example.pavan.favouriteplaces;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.sentry.Sentry;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    LocationManager locationManager;
    LocationListener locationListener;

    private GoogleMap mMap;

    //checking if we have the location permission or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,locationListener);
                Location Lastlocation =locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                currentLoc(Lastlocation,"Your Locatation,");
            }
        }
    }

    public void currentLoc(Location location, String title) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(title));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,(float)13.3),3000,null);
            Log.i("Here is Loc", String.valueOf(location));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        Intent intent=getIntent();
        int i = intent.getIntExtra("position",0);
        if(i==0){
            //zoom in on users location
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

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
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);
            }else{
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,locationListener);
                Location Lastlocation =locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                currentLoc(Lastlocation,"Your Locatation,");
            }

        }else{
            Location location = new Location(LocationManager.PASSIVE_PROVIDER);
            location.setLatitude(MainActivity.locations.get(i).latitude);
            location.setLongitude(MainActivity.locations.get(i).longitude);
            currentLoc(location,MainActivity.places.get(i));
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String msg="";
        try {
            //geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            List<Address> list = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            Log.i("List",list.toString());
            if(list.get(0)!=null && list.size() >0) {
                if (list.get(0).getThoroughfare() != null) {
                    if (list.get(0).getSubThoroughfare() != null) {
                        msg += list.get(0).getSubThoroughfare() + " ";
                    }
                    msg += list.get(0).getThoroughfare();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(msg.equals("") || msg.equals("Unnamed Road")){
            SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy hh:mm");
            msg=sdf.format(new Date());
        }

        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.pavan.favouriteplaces",MODE_PRIVATE);

        mMap.addMarker(new MarkerOptions().position(latLng).title(msg).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        MainActivity.places.add(msg);
        MainActivity.locations.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();


        try {
            ArrayList<String> latitudes=new ArrayList<>();
            ArrayList<String> longitudes=new ArrayList<>();

            for(LatLng ll:MainActivity.locations){
                latitudes.add(Double.toString(ll.latitude));
                longitudes.add(Double.toString(ll.longitude));
            }

            sharedPreferences.edit().putString("places",ObjectSerializer.serialize(MainActivity.places)).apply();
            sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString("longs",ObjectSerializer.serialize(longitudes)).apply();


        } catch (Exception e) {
            e.printStackTrace();
            Sentry.capture(e);
        }

        Toast.makeText(this,"Location Saved!!",Toast.LENGTH_SHORT).show();

    }
}
