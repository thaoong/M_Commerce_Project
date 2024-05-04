package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.WishlistBookAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.CartItem;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private ListView lvFavoBook;
    private ArrayList<Book> dsBook;
    private WishlistBookAdapter adapterBook;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private String userId;
    CheckBox checkBoxAll;
    Button btnProceedBuy;


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
        btnProceedBuy = findViewById(R.id.btnProceedBuy);
        btnProceedBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyProductActivity(v);
            }
        });

        checkBoxAll = findViewById(R.id.checkBox3);
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = checkBoxAll.isChecked();
                for (Book book : dsBook) {
                    book.setBuy(isChecked);
                }
                adapterBook.notifyDataSetChanged();
            }
        });
        lvFavoBook = findViewById(R.id.lvFavoBook);
        dsBook = new ArrayList<>();
        adapterBook = new WishlistBookAdapter(this, dsBook);
        lvFavoBook.setAdapter(adapterBook);
    }

    public void buyProductActivity(View view) {
        ArrayList<CartItem> selectedCartItems = new ArrayList<>();
        for (Book book : dsBook) {
            if (book.isBuy()) {
                CartItem cartItem = new CartItem(book.getId(), book.getName(), book.getUnitPrice(), book.getImageLink().get(0), book.getOldPrice(), 1);
                selectedCartItems.add(cartItem);
            }
        }

        if (selectedCartItems!= null &&!selectedCartItems.isEmpty()) {
            Intent intent = new Intent(this, PrePaymentActivity.class);
            intent.putParcelableArrayListExtra("selectedItems", selectedCartItems);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please choose a product to proceed with buying", Toast.LENGTH_SHORT).show();
        }
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