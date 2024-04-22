package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.thestore.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView rvSearchResult;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private String query;
    private ImageView btnBack, btnEmptySearchBar;
    private EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);
        btnBack = findViewById(R.id.btnBack);
        btnEmptySearchBar = findViewById(R.id.btnEmptySearchBar);

        rvSearchResult = findViewById(R.id.rvSearchResult);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);
        edtSearch = findViewById(R.id.edt_Search);

        rvSearchResult.setLayoutManager(new GridLayoutManager(this,2));
        rvSearchResult.setAdapter(bookAdapter);

        query = getIntent().getStringExtra("QUERY");
        edtSearch.setText(query);
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnEmptySearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
    }
    }
