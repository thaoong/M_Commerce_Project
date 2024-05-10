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
import com.nguyenthithao.thestore.databinding.ActivityFlashSaleBinding;

import java.util.ArrayList;

public class FlashSaleActivity extends AppCompatActivity {
    ActivityFlashSaleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flash_sale);
        binding = ActivityFlashSaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFlashSale();
    }

    private void loadFlashSale() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarFlashSale.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (book != null && book.getOldPrice() != 0) {
                            items.add(book);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.rvFlashSale.setLayoutManager(new GridLayoutManager(FlashSaleActivity.this, 1));
                        binding.rvFlashSale.setAdapter(new BookAdapter2(items));
                    }
                    binding.progressBarFlashSale.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}