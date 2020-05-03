package com.example.savelife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    int proximityRadius=50000;
    private GoogleMap mMap;
    public static final int RequestPermissionCode = 1;

    Location lastLocation;
    private GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    double latitude,longitude;

    static final int Request_User_Location_Code=99;
    Marker currentUserLocationMarker;


    double x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    public void onClick(View v)
    {
        String hospital="hospital";

        Object transferData[]=new Object[2];
        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();

        switch (v.getId())
        {
            case R.id.btn_search:
                EditText address_field=findViewById(R.id.location_search);
                String address=address_field.getText().toString();

                List<Address> addressList=null;
                MarkerOptions userMarkerOptions=new MarkerOptions();

                if(!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder=new Geocoder(this);

                    try {
                        addressList=geocoder.getFromLocationName(address,6);

                        if(addressList!=null)
                        {
                            for(int i=0;i<addressList.size();i++)
                            {
                                Address userAdress=addressList.get(i);
                                LatLng latLng=new LatLng(userAdress.getLatitude(),userAdress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);

                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                currentUserLocationMarker=mMap.addMarker(userMarkerOptions);
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }
                        }
                        else
                        {
                            Toast.makeText(this,"Location not found !!",Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(this,"Please write a place name",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.hospitals_nearby:
                mMap.clear();
                String url=getUrl(latitude,longitude,hospital);
                transferData[0]=mMap;
                transferData[1]=url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this,"Searching nearby hospitals",Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Showing nearby hospitals",Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private String getUrl(double latitude,double longitude,String nearbyPlaces)
    {
        StringBuilder googleURL =new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+latitude+","+longitude);
        googleURL.append("&radius="+proximityRadius);
        googleURL.append("&type="+nearbyPlaces);
        googleURL.append("&sensor=true");
        googleURL.append("&key="+"AIzaSyCdKEpifGpsryZw0B1WOT6AP5FCOtKNa5Y");

        Log.d("MapsAcitivity","url = "+googleURL.toString());

        return googleURL.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)== PERMISSION_GRANTED)
        {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }


    }

    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!=PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},RequestPermissionCode);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},RequestPermissionCode);

            }
            return false;
        }
        else
            return true;
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length>0&&grantResults[0]==PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)==PERMISSION_GRANTED)
                    {
                        if(googleApiClient==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }









    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        lastLocation=location;
        if(currentUserLocationMarker!=null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your current location");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker=mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,  this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)== PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity","Connection suspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity","Connection is failed "+ connectionResult.getErrorCode());
    }


}
