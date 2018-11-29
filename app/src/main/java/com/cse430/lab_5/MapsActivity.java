package com.cse430.lab_5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Couldnt load map ", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        final LatLng ainshams = new LatLng(30.064782, 31.277714);
//        final LatLng ainshams = new LatLng(30.06518, 31.277977);
        mMap.addMarker(new MarkerOptions().position(ainshams).title("Marker in zefta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ainshams));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(ainshams.latitude, ainshams.longitude))
                .radius(20)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

        final Circle circle = mMap.addCircle(circleOptions);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                Location l = new Location("");
                l.setLatitude(ainshams.latitude);
                l.setLongitude(ainshams.longitude);
                double distance = location.distanceTo(l);
                if(distance <= circle.getRadius()){
                    circle.setFillColor(R.color.colorPrimaryDark);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);

    }



}
