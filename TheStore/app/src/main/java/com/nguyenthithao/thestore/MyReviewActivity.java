package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.ToRateProductAdapter;
import com.nguyenthithao.adapter.ReviewedProductAdapter;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.ToRateBook;
import com.nguyenthithao.model.ReviewedBook;

import java.util.ArrayList;

public class MyReviewActivity extends AppCompatActivity {
    ListView lvToRate, lvReviewed;
    ArrayList<ToRateBook> dsToRate, dsReviewed;
    ToRateProductAdapter adapterToRate;
    ReviewedProductAdapter adapterReviewed;
    TabHost tabHost;
    String reviewerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        displayActionBar();
        addViews();
    }

    private void addViews() {
        lvToRate = findViewById(R.id.lvToRate);
        lvReviewed = findViewById(R.id.lvReviewed);
        dsToRate = new ArrayList<>();
        dsReviewed = new ArrayList<>();
        adapterToRate = new ToRateProductAdapter(MyReviewActivity.this, R.layout.item_rating, dsToRate);
        adapterReviewed = new ReviewedProductAdapter(MyReviewActivity.this, R.layout.item_old_rating);
        lvToRate.setAdapter(adapterToRate);
        lvReviewed.setAdapter(adapterReviewed);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        String toRate = getResources().getString(R.string.strToRate);
        tab1.setIndicator(toRate);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        String myReviews = getResources().getString(R.string.strMyReviews);
        tab2.setIndicator(myReviews);
        tabHost.addTab(tab2);

        customizeTabs(tabHost);
        getToRate();
        getReviewed();
    }

    private void getToRate() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("orders");
            databaseReference.orderByChild("userID").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dsToRate.clear();
                    dsReviewed.clear();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null && order.getStatus().equals("Hoàn tất")) {
                            for (OrderBook book : order.getOrderBooks()) {
                                if (book.isReview()) {
                                    dsReviewed.add(new ToRateBook(
                                            book.getName(),
                                            book.getImageLink(),
                                            book.getUnitPrice(),
                                            book.getOldPrice(),
                                            book.getQuantity(),
                                            book.getId()
                                    ));
                                } else {
                                    dsToRate.add(new ToRateBook(
                                            book.getName(),
                                            book.getImageLink(),
                                            book.getUnitPrice(),
                                            book.getOldPrice(),
                                            book.getQuantity(),
                                            book.getId()
                                    ));
                                }
                            }
                        }
                    }
                    adapterToRate.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi
                }
            });
        } else {
            Intent intent = new Intent(MyReviewActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        lvToRate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToRateBook selectedProduct = dsToRate.get(position);
                String productName = selectedProduct.getName();

                Intent intent = new Intent(MyReviewActivity.this, RatingActivity.class);
                intent.putExtra("productName", productName);
                startActivity(intent);
            }
        });
    }

    private void getReviewed() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            // Get reviewer name

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("books");
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                        String bookName = bookSnapshot.child("name").getValue(String.class);
                        String bookImgUrl = bookSnapshot.child("imageLink").child("0").getValue(String.class);
                        DataSnapshot reviewsSnapshot = bookSnapshot.child("reviews");
                        for (DataSnapshot reviewSnapshot : reviewsSnapshot.getChildren()) {
                            String reviewUserId = reviewSnapshot.child("userId").getValue(String.class);
                            if (reviewUserId != null && reviewUserId.equals(currentUserId)) {
                                String reviewContent = reviewSnapshot.child("comment").getValue(String.class);
                                long rating = reviewSnapshot.child("rating").getValue(long.class);
                                String ratingDate = reviewSnapshot.child("ratingDate").getValue(String.class);

                                // Get the imageUrls and add them to the review object
                                ArrayList<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < 3; i++) {
                                    String imageUrl = reviewSnapshot.child("imageUrls").child(String.valueOf(i)).getValue(String.class);
                                    if (imageUrl != null) {
                                        imageUrls.add(imageUrl);
                                    }
                                }
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            reviewerName = dataSnapshot.child("name").getValue(String.class);
                                            adapterReviewed.add(new ReviewedBook(reviewerName, rating, bookImgUrl, bookName, reviewContent, imageUrls, ratingDate));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle any errors
                                    }
                                });

                            }
                        }
                    }
                    adapterReviewed.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }
    }

    private void customizeTabs(TabHost tabHost) {
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            LinearLayout tabLayout = (LinearLayout) tabWidget.getChildAt(i);
            TextView tabTextView = (TextView) tabLayout.getChildAt(1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabTextView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            tabTextView.setTextColor(Color.parseColor("#5C3507"));

            tabTextView.setLayoutParams(layoutParams);
        }
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strMyReview);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>" + title + "</font>"));
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
