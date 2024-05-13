package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

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
    private BookAdapter2 bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_arrivals);
        binding = ActivityNewArrivalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bookAdapter = new BookAdapter2(NewArrivalsActivity.this, R.layout.item_book);
        binding.lvNewArrivals.setAdapter(bookAdapter);
        displayActionBar();
        loadNewArrivals();
        addEvents();
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>NEW ARRIVALS</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        binding.lvNewArrivals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookAdapter.getItem(position);
                Intent intent = new Intent(NewArrivalsActivity.this, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", book);
                startActivity(intent);
            }
        });
    }
    private void loadNewArrivals() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        myRef.limitToLast(7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        bookAdapter.add(issue.getValue(Book.class));
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