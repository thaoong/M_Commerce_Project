package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.OrderDetailAdapterTest;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.OrderDetailTest;
import com.nguyenthithao.model.Rating;
import com.nguyenthithao.thestore.databinding.ActivityRatingBinding;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    ActivityRatingBinding binding;
    ArrayList<OrderDetailTest> dsOrderDetail;
    OrderDetailAdapterTest adapterOrderDetail;
    private ArrayList<String> imagePathList = new ArrayList<>();
    private ArrayList<View> imageViewList = new ArrayList<>();
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rating);

        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        RatingBar ratingBar1 = findViewById(R.id.ratingBar1);
        RatingBar ratingBar2 = findViewById(R.id.ratingBar2);
        ratingBar.setRating(5);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        float productPrice = intent.getFloatExtra("productPrice", 0);
        int productQuantity = intent.getIntExtra("productQuantity", 0);
        String productImage = intent.getStringExtra("productImage");

        TextView txtProductName = findViewById(R.id.txt_ProductName);
        txtProductName.setText(productName);

        displayActionBar();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders");
        dsOrderDetail = new ArrayList<>();
        adapterOrderDetail = new OrderDetailAdapterTest(RatingActivity.this, R.layout.item_rating, dsOrderDetail);

        // Set adapter cho ListView
        //binding.lvOrderDetail.setAdapter(adapterOrderDetail);

        eventsRating();
    }

    private void eventsRating() {
        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String description = getDescriptionForRating(rating);
                binding.txtRatingDescription.setText(description);
            }
        });

        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePathList.size() < MAX_IMAGE_COUNT) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(RatingActivity.this, "Only maximum 3 images can be selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String description = getDescriptionForRating(rating);
                binding.txtRatingDescription1.setText(description);
            }
        });

        binding.ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String description = getDescriptionForRating(rating);
                binding.txtRatingDescription2.setText(description);
            }
        });

        binding.btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = binding.ratingBar.getRating();
                String comment = binding.edtReview.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Rating review = new Rating(userId, rating, comment);

                String bookId = "book40";
                DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(bookId);

                bookRef.child("reviews").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookRef.child("reviews").push().setValue(review);
                        Toast.makeText(RatingActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RatingActivity.this, MyReviewActivity.class);
                        intent.putExtra("bookId", bookId);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu có
                        Toast.makeText(RatingActivity.this, "Failed to submit rating!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private String getDescriptionForRating(float rating) {
        String description = "";
        switch ((int) rating) {
            case 1:
                description = "Bad";
                break;
            case 2:
                description = "Poor";
                break;
            case 3:
                description = "Fair";
                break;
            case 4:
                description = "Good";
                break;
            case 5:
                description = "Amazing";
                break;
        }
        return description;
    }

    private void pickImageFromGallery() {
        if (imagePathList.size() < MAX_IMAGE_COUNT) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } else {
            Toast.makeText(RatingActivity.this, "Only maximum 3 images can be selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strRating);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imagePath = getPathFromUri(imageUri);
            if (!imagePathList.contains(imagePath)) {
                imagePathList.add(imagePath);
                displaySelectedImages();
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void displaySelectedImages() {
        binding.layoutSelectedImages.removeAllViews();
        imageViewList.clear();

        for (int i = 0; i < imagePathList.size(); i++) {
            View imageItemView = getLayoutInflater().inflate(R.layout.item_image_remove, null);
            ImageView imageView = imageItemView.findViewById(R.id.imageView);
            ImageView removeImageView = imageItemView.findViewById(R.id.removeImageView);

            String imagePath = imagePathList.get(i);
            imageView.setImageURI(Uri.parse(imagePath));

            int finalI = i;
            removeImageView.setOnClickListener(v -> {
                imagePathList.remove(finalI);
                displaySelectedImages();
            });

            imageViewList.add(imageItemView);
            binding.layoutSelectedImages.addView(imageItemView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsOrderDetail.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && order.getStatus().equals("Hoàn tất")) {
                        String orderDate = order.getOrderDate();
                        binding.txtDate.setText(orderDate);
                        for (OrderBook book : order.getOrderBooks()) {
                            dsOrderDetail.add(new OrderDetailTest(
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
                adapterOrderDetail.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}