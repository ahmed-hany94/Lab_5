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
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    boolean entered1, entered2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        entered1 = entered2 = false;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            myMap = googleMap;
            Toast.makeText(this, "Couldn't load map, Permission must be granted. ", Toast.LENGTH_LONG).show();
            return;
        }
        start(googleMap);
    }

    private void start(GoogleMap mMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        final LatLng ainshams = new LatLng(30.064782, 31.277714);
        final LatLng ainshams2 = new LatLng(30.06518, 31.277977);
        mMap.addMarker(new MarkerOptions().position(ainshams).title("Marker1"));
        mMap.addMarker(new MarkerOptions().position(ainshams2).title("Marker2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ainshams2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ainshams));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(ainshams.latitude, ainshams.longitude))
                .radius(20)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

        CircleOptions circleOptions2 = new CircleOptions()
                .center(new LatLng(ainshams2.latitude, ainshams2.longitude))
                .radius(20)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

        final Circle circle = mMap.addCircle(circleOptions);
        final Circle circle2 = mMap.addCircle(circleOptions2);
        final Location l1 = new Location("");
        final Location l2 = new Location("");
        l1.setLatitude(ainshams.latitude);
        l1.setLongitude(ainshams.longitude);
        l2.setLatitude(ainshams2.latitude);
        l2.setLongitude(ainshams2.longitude);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                if (!entered1) {
                    double distance = location.distanceTo(l1);
                    if (distance <= circle.getRadius()) {
                        circle.setFillColor(R.color.colorPrimaryDark);
                        entered1 = true;
                        check();
                    }
                }

                if (!entered2) {
                    double distance2 = location.distanceTo(l2);
                    if (distance2 <= circle2.getRadius()) {
                        circle2.setFillColor(R.color.colorPrimaryDark);
                        entered2 = true;
                        check();
                    }
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


    private void check() {
        if (entered1 && entered2) {
            Toast toast = Toast.makeText(this, "Congratulations, you have collected all coins.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            start(myMap);

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }
}