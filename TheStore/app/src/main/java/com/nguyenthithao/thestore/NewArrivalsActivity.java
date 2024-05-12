package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.BookAdapter2;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.databinding.ActivityNewArrivalsBinding;

import java.util.ArrayList;

public class NewArrivalsActivity extends AppCompatActivity {
    ActivityNewArrivalsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_arrivals);
        binding = ActivityNewArrivalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadNewArrivals();
    }

    private void loadNewArrivals() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarNewArrivals.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.limitToLast(7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Book.class));
                    }
                    if (!items.isEmpty()) {
//                        binding.rvNewArrivals.setLayoutManager(new LinearLayoutManager(NewArrivalsActivity.this));
//                        binding.rvNewArrivals.setAdapter(new BookAdapter2(items));
                    }
                }
                binding.progressBarNewArrivals.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}