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
import com.nguyenthithao.adapter.OrderDetailAdapterTest;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.OrderDetailTest;
import com.nguyenthithao.thestore.R;

import java.util.ArrayList;

public class MyReviewActivity extends AppCompatActivity {
    ListView lvToRate, lvReviewed;
    ArrayList<OrderDetailTest> dsToRate, dsReviewed;
    OrderDetailAdapterTest adapterToRate, adapterReviewed;
    TabHost tabHost;

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
        adapterToRate = new OrderDetailAdapterTest(MyReviewActivity.this, R.layout.item_rating, dsToRate);
        adapterReviewed = new OrderDetailAdapterTest(MyReviewActivity.this, R.layout.item_old_rating, dsReviewed);
        lvToRate.setAdapter(adapterToRate);
        lvReviewed.setAdapter(adapterReviewed);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("To Rate");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("My Reviews");
        tabHost.addTab(tab2);

        customizeTabs(tabHost);

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
                                    dsReviewed.add(new OrderDetailTest(
                                            book.getName(),
                                            book.getImageLink(),
                                            book.getUnitPrice(),
                                            book.getOldPrice(),
                                            book.getQuantity(),
                                            book.getId(),
                                            order.getOrderDate()
                                    ));
                                } else {
                                    dsToRate.add(new OrderDetailTest(
                                            book.getName(),
                                            book.getImageLink(),
                                            book.getUnitPrice(),
                                            book.getOldPrice(),
                                            book.getQuantity(),
                                            book.getId(),
                                            order.getOrderDate()
                                    ));
                                }
                            }
                        }
                    }

                    adapterToRate.notifyDataSetChanged();
                    adapterReviewed.notifyDataSetChanged();
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
                OrderDetailTest selectedProduct = dsToRate.get(position);
                String productName = selectedProduct.getName();

                Intent intent = new Intent(MyReviewActivity.this, RatingActivity.class);
                intent.putExtra("productName", productName);
                startActivity(intent);
            }
        });

        lvReviewed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderDetailTest selectedProduct = dsReviewed.get(position);
                String productName = selectedProduct.getName();

                Intent intent = new Intent(MyReviewActivity.this, RatingActivity.class);
                intent.putExtra("productName", productName);
                startActivity(intent);
            }
        });
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
