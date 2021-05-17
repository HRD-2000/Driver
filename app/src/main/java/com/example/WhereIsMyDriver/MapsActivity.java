package com.example.WhereIsMyDriver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Button appCompatButton1;
    GoogleMap mMap;
    String route_id;
    SupportMapFragment mapFragment;
    Marker marker;
    LocationBroadcastReceiver receiver;
    public static final int REQUEST_CHECK_SETTING = 1;
    FusedLocationProviderClient fusedLocationProviderClient;

    LatLng eva = new LatLng(22.2733889,73.1877028);
    LatLng me;
    Double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        appCompatButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,Home.class);
                intent.putExtra("route_id",route_id);
                startActivity(intent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        receiver = new LocationBroadcastReceiver();

        /*if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_SETTING);
            } else {
                //Req Location Permission

                startLocService();
            }
        } else {
            //Start the Location Service
            startLocService();
            mMap.setMyLocationEnabled(true);
        }*/
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent i= getIntent();
        route_id = i.getStringExtra("route_id");

    }

    void startLocService() {
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(receiver, filter);
        Intent intent = new Intent(MapsActivity.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startLocService();
                } else {
                    Toast.makeText(this, "Give me permissions", Toast.LENGTH_LONG).show();
                }
        }*/

        if (requestCode == REQUEST_CHECK_SETTING){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                startLocService();
                zoomToUserLocation();
                Log.d("test", "onRequestPermissionsResult: if block for alert box");

            }else {
                //dialog here...
                Log.d("test", "onRequestPermissionsResult: else block for alert box");

            }
        }
    }
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            Log.d("test", "onMapReady: 1st if block");
            enableUserLocation();
            startLocService();
            zoomToUserLocation();
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                //dialog here for permission
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CHECK_SETTING);
                Log.d("test", "onMapReady: if block in else part");
            }else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CHECK_SETTING);
                Log.d("test", "onMapReady: else block in else part");
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getPermission();

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */



        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation(){
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    @SuppressLint("MissingPermission")
    private void zoomToUserLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause invoked", Toast.LENGTH_SHORT).show();
        unregisterReceiver(receiver);
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACT_LOC")) {
                double lat = intent.getDoubleExtra("latitude", 0f);
                double longitude = intent.getDoubleExtra("longitude", 0f);
                float bearing = intent.getFloatExtra("bearing",0);
                float bearingAccuracy = intent.getFloatExtra("BearingAccuracy",0);
                float Accuracy = intent.getFloatExtra("Accuracy",0);
                if (mMap != null) {
                    LatLng latLng = new LatLng(lat, longitude);
                    //me = new LatLng(lat, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    if (marker != null)
                        marker.setPosition(latLng);
                    else
                        //marker = mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
                //me = new LatLng(lat, longitude);
                //distance = SphericalUtil.computeDistanceBetween(eva, me);
               // Log.d("distance", "eva: "+eva+"\n me: "+me);
                //Toast.makeText(this, "Distance between Sydney and Brisbane is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();
               // Log.d("distance", "distance: "+distance+"\n"+"Distance between eva and your location is \n " + String.format("%.2f", distance / 1000) + "km");

                Toast.makeText(MapsActivity.this, "Latitude is: " + lat + ", Longitude is " + longitude, Toast.LENGTH_SHORT).show();
                Log.d("test", "Latitude: "+lat+"\nLongitude: "+longitude+"\nBearing: "+bearing+"\nBearingAccuracy: "+bearingAccuracy+"\nAccuracy :"+Accuracy);

                Helper helper = new Helper(longitude,lat,bearing,bearingAccuracy,Accuracy);

                FirebaseDatabase.getInstance().getReference("Route "+route_id).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MapsActivity.this, "Location Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Location Not Saved", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }
    }

    
}



