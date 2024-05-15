package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SupportActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private GoogleMap gMap;
//    private TextView tvDistance;

    private DatabaseReference locationRef;
    private FusedLocationProviderClient fusedLocationClient;
    private List<LatLng> storeLocations = new ArrayList<>();
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        displayActionBar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRef = FirebaseDatabase.getInstance().getReference().child("location");

        // Lấy tọa độ ngay khi Activity được tạo
        fetchLocationsFromDatabase();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            getLastKnownLocation();
        } else {
            requestLocationPermission();
        }
    }

    private void fetchLocationsFromDatabase() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storeLocations.clear();

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    double latitude = locationSnapshot.child("latitude").getValue(Double.class);
                    double longitude = locationSnapshot.child("longitude").getValue(Double.class);
                    LatLng location = new LatLng(latitude, longitude);
                    storeLocations.add(location);
                    gMap.addMarker(new MarkerOptions().position(location));
                }

                // Ensure there are points to include in the bounds
                if (!storeLocations.isEmpty()) {
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    for (LatLng location : storeLocations) {
                        boundsBuilder.include(location);
                    }
                    LatLngBounds bounds = boundsBuilder.build();
                    LatLng center = bounds.getCenter();

                    // Find the nearest store location and draw a route to it
                    if (ContextCompat.checkSelfPermission(SupportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(SupportActivity.this, location -> {
                                    if (location != null) {
                                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                        gMap.addMarker(new MarkerOptions().position(userLocation).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                        LatLng nearestStoreLocation = findNearestStoreLocation(userLocation);
                                        if (nearestStoreLocation != null) {
                                            drawRoute(userLocation, nearestStoreLocation);
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private LatLng findNearestStoreLocation(LatLng userLocation) {
        LatLng nearestLocation = null;
        float minDistance = Float.MAX_VALUE;

        for (LatLng storeLocation : storeLocations) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                    storeLocation.latitude, storeLocation.longitude, results);

            if (results[0] < minDistance) {
                minDistance = results[0];
                nearestLocation = storeLocation;
            }
        }

        return nearestLocation;
    }

//    private void drawRoute(LatLng userLocation, LatLng storeLocation) {
//        getDirections(userLocation, storeLocation, gMap);
//    }
//
//    private void getDirections(LatLng origin, LatLng destination, GoogleMap map) {
//        String apiKey = "AIzaSyAEdvqdd8cVENajarhcxqqwNGZqqBEN2Ao"; // Đổi bằng API key của bạn
//
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
//                origin.latitude + "," + origin.longitude +
//                "&destination=" + destination.latitude + "," + destination.longitude +
//                "&key=" + apiKey;
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//                try {
//                    String jsonData = response.body().string();
//                    JSONObject jsonObject = new JSONObject(jsonData);
//                    JSONArray routes = jsonObject.getJSONArray("routes");
//
//                    if (routes.length() > 0) {
//                        JSONObject route = routes.getJSONObject(0);
//                        JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
//                        String points = overviewPolyline.getString("points");
//
//                        List<LatLng> decodedPath = decodePoly(points);
//
//                        runOnUiThread(() -> {
//                            map.clear();
//                            PolylineOptions polylineOptions = new PolylineOptions()
//                                    .addAll(decodedPath)
//                                    .color(Color.RED)
//                                    .width(10)
//                                    .geodesic(true);
//
//                            map.addPolyline(polylineOptions);
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private List<LatLng> decodePoly(String encoded) {
//        List<LatLng> poly = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++);
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//
//            int dlat = ((result & 1)!= 0? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++);
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//
//            int dlng = ((result & 1)!= 0? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
//            poly.add(p);
//        }
//
//        return poly;
//    }
private void drawRoute(LatLng userLocation, LatLng storeLocation) {
    // Here you would typically use Google Directions API to get the route,
    // but for simplicity, let's just draw a straight line.
    PolylineOptions polylineOptions = new PolylineOptions()
            .add(userLocation)
            .add(storeLocation)
            .color(Color.RED)
            .width(10);

    gMap.addPolyline(polylineOptions);

    // Zoom and center the map on the route
    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
    boundsBuilder.include(userLocation);
    boundsBuilder.include(storeLocation);
    LatLngBounds bounds = boundsBuilder.build();

    int padding = 100; // Offset from edges of the map in pixels
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
    gMap.animateCamera(cu);
}

    private void getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                gMap.addMarker(new MarkerOptions().position(userLocation).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                if (!storeLocations.isEmpty()) {
                                    LatLng nearestStoreLocation = findNearestStoreLocation(userLocation);
                                    if (nearestStoreLocation != null) {
                                        drawRoute(userLocation, nearestStoreLocation);
                                    }
                                }
                                fetchLocationsFromDatabase();
                            }
                        }
                    });
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gMap.setMyLocationEnabled(true);
                    getLastKnownLocation();
                }
            }
        }
    }
    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Support</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}