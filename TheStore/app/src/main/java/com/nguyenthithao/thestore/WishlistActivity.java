package com.nguyenthithao.thestore;

//import static android.os.Build.VERSION_CODES.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.model.Book;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView lvFavoBook;
    private ArrayList<Book> dsBook;
    private BookAdapter adapterBook;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        displayActionBar();
        addViews();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        loadWishlistBooks();
    }

    private void addViews() {
        lvFavoBook = findViewById(R.id.lvFavoBook);
        lvFavoBook.setLayoutManager(new GridLayoutManager(this, 2));

        dsBook = new ArrayList<>();
        adapterBook = new BookAdapter(dsBook);
        lvFavoBook.setAdapter(adapterBook);
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Sản phẩm yêu thích</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadWishlistBooks() {
        mDatabaseReference.child("wishlists").child(userId).child("books")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dsBook.clear();
                        for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                            String bookId = bookSnapshot.getKey();
                            DatabaseReference bookRef = mDatabaseReference.child("books").child(bookId);
                            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot bookDataSnapshot) {
                                    Book book = bookDataSnapshot.getValue(Book.class);
                                    if (book != null) {
                                        dsBook.add(book);
                                        adapterBook.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }
}