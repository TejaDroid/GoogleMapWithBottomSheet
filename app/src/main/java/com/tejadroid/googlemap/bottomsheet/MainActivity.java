package com.tejadroid.googlemap.bottomsheet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Double Latitude = 72.585;
    private Double Longitude = 23.0339;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<LatLng> arrayList;
    private Map<LatLng, LatLng> latLngMap;
    private Marker m1;
    private Marker m2;
    private Marker marker;
    private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LinearLayout llMain = (LinearLayout) findViewById(R.id.llbottomsheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(llMain);

        arrayList = new ArrayList<>();
        arrayList.add(new LatLng(Latitude, Longitude));
        arrayList.add(new LatLng(22.303194, 70.802162));
        arrayList.add(new LatLng(22.404294, 70.802262));
        arrayList.add(new LatLng(22.505394, 70.802362));
        arrayList.add(new LatLng(22.606494, 70.802462));
        arrayList.add(new LatLng(22.707594, 70.802562));
        arrayList.add(new LatLng(22.808694, 70.802662));
        arrayList.add(new LatLng(22.909794, 70.802762));
        arrayList.add(new LatLng(22.101894, 70.802862));
        arrayList.add(new LatLng(22.202994, 70.802962));

        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Toast.makeText(getApplicationContext(), "STATE_EXPANDED", Toast.LENGTH_LONG).show();
                    googleMap.setPadding(0, 0, 0, bottomSheet.getHeight());
//                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
                    clusterMarker();
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Toast.makeText(getApplicationContext(), "STATE_COLLAPSED", Toast.LENGTH_LONG).show();
                    googleMap.setPadding(0, 0, 0, 310);
//                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                    clusterMarker();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        ViewPager viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, arrayList);
        viewPager.setAdapter(myPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                googleMap.clear();
                if (position < 9 && position >= 1) {
                    m1 = googleMap.addMarker(new MarkerOptions().position(arrayList.get(position)).title("Source"));
                    m2 = googleMap.addMarker(new MarkerOptions().position(arrayList.get(position + 1)).title("Destination"));
                    googleMap.addPolyline(
                            new PolylineOptions().add((arrayList.get(position)), (arrayList.get(position + 1))
                            ).width(2).color(Color.BLUE).geodesic(true)
                    );
                    clusterMarker();
                } else {
                    marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Marker in Verve"));
                    marker.setTag("currentLocation");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.0f));
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Logic to handle location object
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();

                    LatLng sydney = new LatLng(Latitude, Longitude);
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Verve"));
                    marker.setTag("currentLocation");
                    googleMap.setPadding(0, 0, 0, 310);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));

                } else {
                    Toast.makeText(getApplicationContext(), "Location not Found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void clusterMarker() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (m1 != null && m2 != null) {
            builder.include(m1.getPosition());
            builder.include(m2.getPosition());
        } else {
            builder.include(marker.getPosition());
        }
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }
}
