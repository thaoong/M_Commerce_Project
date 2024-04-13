package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.SliderAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.SliderItems;
import com.nguyenthithao.thestore.databinding.ActivityProductDetailBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    private Book selectedBook;
    private Handler slideHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_detail);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        displayActionBar();
        setContentView(binding.getRoot());
        getBookImages();
        getBookDetails();
        getRelatedProducts();
    }

    private void getBookImages() {
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < selectedBook.getImageLink().size(); i++) {
            sliderItems.add(new SliderItems(selectedBook.getImageLink().get(i)));
        }
        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }

    private void getRelatedProducts() {
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");
        String category = selectedBook.getCategory();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        ArrayList<Book> items = new ArrayList<>();
        myRef.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (!Objects.equals(selectedBook.getName(), book.getName())) {
                            items.add(book);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.rvRelatedProduct.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvRelatedProduct.setAdapter(new BookAdapter(items));
                    }
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
//        String title = getResources().getString(R.string.strEditAddress);
        String title = "Product Details";
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuCart) {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.mnuSearch) {
            Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookDetails() {
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");
        binding.txtTittle.setText(selectedBook.getName());
        binding.txtRating.setText(selectedBook.getRating()+"");
        binding.ratingBar.setRating(selectedBook.getRating());
        binding.txtNumofComment.setText("("+selectedBook.getReviewNum()+")");

        float unitPrice = selectedBook.getUnitPrice();
        String formattedUnitPrice = formatCurrency(unitPrice);
        binding.txtUnitPrice.setText(formattedUnitPrice + "đ");

        float oldPrice = selectedBook.getOldPrice();
        if (oldPrice != 0) {
            String formattedOldPrice = formatCurrency(oldPrice);
            binding.txtOldPrice.setText(formattedOldPrice+ "đ");
            binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            float salePercent = ((oldPrice - unitPrice) / oldPrice) * 100;
            binding.txtSalePercent.setText(String.format("-%.0f%%", salePercent));
        }
        else {
            binding.txtOldPrice.setVisibility(View.GONE);
            binding.txtSalePercent.setVisibility(View.GONE);
        }

        binding.txtAuthor.setText(selectedBook.getAuthor());
        binding.txtPublicationDate.setText(selectedBook.getPublicationDate());
        binding.txtDescription.setText(selectedBook.getDescription());

        // Get category name from category table
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference categoryRef = firebaseDatabase.getReference().child("categories").child(selectedBook.getCategory());
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String categoryName = dataSnapshot.child("name").getValue(String.class);
                    binding.txtCategory.setText(categoryName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String formatCurrency(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }
}