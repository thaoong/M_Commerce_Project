package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.CommentAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.model.ReviewedBook;

import java.util.ArrayList;

public class ReviewBookActivity extends AppCompatActivity {
    CommentAdapter commentAdapter;
    ListView lvComment;
    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_book);
        lvComment = findViewById(R.id.lvComment);
        displayActionBar();
        loadReviews();
    }

    private void loadReviews() {
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK_REVIEW");
        String selectedBookId = selectedBook.getId();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("books").child(selectedBookId).child("reviews");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    String reviewContent = reviewSnapshot.child("comment").getValue(String.class);
                    long rating = reviewSnapshot.child("rating").getValue(long.class);
                    String userId = reviewSnapshot.child("userId").getValue(String.class);

                    // Get the imageUrls and add them to the review object
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        String imageUrl = reviewSnapshot.child("imageUrls").child(String.valueOf(i)).getValue(String.class);
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    //Get reviewerName
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String reviewerName = dataSnapshot.child("name").getValue(String.class);
                                commentAdapter.add(new ReviewedBook(reviewerName, rating, "", "", reviewContent, imageUrls));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        commentAdapter = new CommentAdapter(ReviewBookActivity.this, R.layout.item_comment);
        lvComment.setAdapter(commentAdapter);
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK_REVIEW");
        String title = selectedBook.getName();
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