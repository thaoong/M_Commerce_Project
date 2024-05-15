package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.nguyenthithao.thestore.databinding.ActivityBestSellingBinding;

import java.util.ArrayList;

public class BestSellingActivity extends AppCompatActivity {
    ActivityBestSellingBinding binding;
    private BookAdapter2 bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBestSellingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bookAdapter = new BookAdapter2(BestSellingActivity.this, R.layout.item_book);
        binding.lvBestSelling.setAdapter(bookAdapter);
        displayActionBar();
        loadBestSelling();
        addEvents();
    }

    private void addEvents() {
        binding.lvBestSelling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookAdapter.getItem(position);
                Intent intent = new Intent(BestSellingActivity.this, ProductDetailActivity.class);
                intent.putExtra("SELECTED_BOOK", book);
                startActivity(intent);
            }
        });
    }

    private void loadBestSelling() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarBestSelling.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (book != null && book.getBestSelling() == 1) {
                            bookAdapter.add(book);
                        }
                    }
                    binding.progressBarBestSelling.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strBESTSELLING);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
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