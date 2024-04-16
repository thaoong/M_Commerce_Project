package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.model.Author;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.databinding.ActivityAuthorBookBinding;
import com.nguyenthithao.thestore.databinding.ActivityCategoryBookBinding;

import java.util.ArrayList;

public class AuthorBookActivity extends AppCompatActivity {
    ActivityAuthorBookBinding binding;
    private Author selectedAuthor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_author_book);
        binding = ActivityAuthorBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        loadBookByAuthor();
    }

    private void loadBookByAuthor() {
        selectedAuthor = (Author) getIntent().getSerializableExtra("SELECTED_AUTHOR");
        String authorName = selectedAuthor.getName();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.orderByChild("author").equalTo(authorName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        items.add(book);
                    }
                    if (!items.isEmpty()) {
                        binding.rvBook.setLayoutManager(new GridLayoutManager(AuthorBookActivity.this, 2));
                        binding.rvBook.setAdapter(new BookAdapter(items));
                    }
                    binding.progressBar.setVisibility(View.GONE);
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
        selectedAuthor = (Author) getIntent().getSerializableExtra("SELECTED_AUTHOR");
        String title = selectedAuthor.getName();
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