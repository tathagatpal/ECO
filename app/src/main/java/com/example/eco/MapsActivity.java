package com.example.eco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final int RC_SIGN_IN =123 ;
    //auth variables
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;


    private HashMap<Marker, MarkerInfo> MarkerMap;
    private HashMap<Marker, String> imgList;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Opening Map", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;
        MarkerMap = new HashMap<Marker, MarkerInfo>();
        imgList = new HashMap<Marker, String>();
        if(mLocationPermissionGranted){
            getDeviceLocation();

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);


            ChildEventListener cEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    MarkerInfo mInfo = dataSnapshot.getValue(MarkerInfo.class);
                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(mInfo.getLat(),mInfo.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.lm_foreground)));
                    MarkerMap.put(m,mInfo);
                    imgList.put(m,dataSnapshot.getKey()+ ".jpg");
                    //Toast.makeText(MapsActivity.this,"uouououu",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            database.addChildEventListener(cEventListener);
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private boolean mLocationPermissionGranted = false;
    GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermission();
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            //signed in
            user = auth.getCurrentUser();
        }
        else{
            //not signed in
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        }


    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                        } else{
                            Log.d(TAG, "onComplete: Current Location is null");
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.e(TAG, "getDevideLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom){
        Log.d(TAG, "moveCamera: moving camera to: Lat: " + latlng.latitude + ", Lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }


    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapsActivity.this);
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting  location permissions");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else{
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 ){
                    for(int i=0; i<grantResults.length; i++){
                        if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    //initialize map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        Toast.makeText(MapsActivity.this,
//                "Location Marked",
//                Toast.LENGTH_SHORT).show();
//
//        //Add marker on LongClick position
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(latLng.toString())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.lm_foreground));
//        mMap.addMarker(markerOptions);
//
        Intent myIntent = new Intent(MapsActivity.this, Cam.class);
        myIntent.putExtra("lat", latLng.latitude);
        myIntent.putExtra("lon", latLng.longitude);
        myIntent.putExtra("uid",user.getUid());
        myIntent.putExtra("name",user.getDisplayName());
        startActivity(myIntent);
//        MarkerInfo mInfo = new MarkerInfo(latLng.latitude, latLng.longitude, user.getUid(),user.getDisplayName(), 0,0);
//        database.push().setValue(mInfo);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(MapsActivity.this, "Wait for Info" , Toast.LENGTH_LONG).show();
        Intent mIntent = new Intent(MapsActivity.this, MarkerData.class);
        mIntent.putExtra("name",MarkerMap.get(marker).getUserName());
        mIntent.putExtra("rating",MarkerMap.get(marker).getRating());
        mIntent.putExtra("fName", imgList.get(marker));
        startActivity(mIntent);
        return false;
    }
}

