package com.example.savelife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    public static final int RequestPermissionCode=1;

    GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;

    TextView latitude, longitude;
    double x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        fusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(this);
        latitude=findViewById(R.id.lat_value);
        longitude=findViewById(R.id.lon_value);
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
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }
    @Override
    protected void onStop()
    {

        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!=PERMISSION_GRANTED){
            requestPermission();
        }
        else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));

                        x=location.getLatitude();
                        y=location.getLongitude();
                        LatLng new_place = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(new_place).title("Maker in your place"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new_place));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        System.out.println(new_place);
                    }
                }
            });
        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{ACCESS_FINE_LOCATION},RequestPermissionCode);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity","Connection suspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity","Connection is failed "+ connectionResult.getErrorCode());
    }

    public void searchLocation(View view) {
        EditText locationSearch =  findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }

}
