package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.model.Book;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView rvSearchResult;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        rvSearchResult = findViewById(R.id.rvSearchResult);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);

        rvSearchResult.setLayoutManager(new GridLayoutManager(this,2));
        rvSearchResult.setAdapter(bookAdapter);

        query = getIntent().getStringExtra("QUERY");
        searchBooks(query);
    }

    private void searchBooks(String query) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference booksRef = firebaseDatabase.getReference("books");

        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book.getName().toLowerCase().contains(query.toLowerCase())) {
                        bookList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    }
