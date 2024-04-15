package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SupportActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private DatabaseReference locationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        // Khởi tạo DatabaseReference cho bảng "location"
        locationRef = FirebaseDatabase.getInstance().getReference().child("location");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Thiết lập vị trí ban đầu của bản đồ cho Việt Nam
        LatLng vietnam = new LatLng(16.0, 107.5);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vietnam, 6));

        // Lấy dữ liệu từ Firebase và đánh dấu vị trí trên bản đồ
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LatLngBounds.Builder builder = LatLngBounds.builder();

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    double latitude = locationSnapshot.child("latitude").getValue(Double.class);
                    double longitude = locationSnapshot.child("longitude").getValue(Double.class);
                    LatLng location = new LatLng(latitude, longitude);
                    gMap.addMarker(new MarkerOptions().position(location));
                    builder.include(location);
                }

                if (!dataSnapshot.getChildren().iterator().hasNext()) {
                    // Không có marker nào
                    return;
                }

                LatLngBounds bounds = builder.build();
                int padding = 100; // Khoảng đệm (theo đơn vị điểm ảnh)
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}