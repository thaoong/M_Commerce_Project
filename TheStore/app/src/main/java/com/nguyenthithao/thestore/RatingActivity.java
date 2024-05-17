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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenthithao.adapter.OrderDetailAdapterTest;
import com.nguyenthithao.model.Order;
import com.nguyenthithao.model.OrderBook;
import com.nguyenthithao.model.OrderDetailTest;
import com.nguyenthithao.model.Rating;
import com.nguyenthithao.thestore.databinding.ActivityRatingBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    ActivityRatingBinding binding;
    ArrayList<OrderDetailTest> dsOrderDetail;
    OrderDetailAdapterTest adapterOrderDetail;
    private ArrayList<Uri> imageUriList = new ArrayList<>();
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    DatabaseReference mDatabase;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ratingBar.setRating(5);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productImage = intent.getStringExtra("productImage");
        if (productName != null) {
            binding.txtProductName.setText(productName);
        } else {
            binding.txtProductName.setText("Product name not available");
        }
        Picasso.get().load(productImage).into(binding.imgProduct);

        float productPrice = intent.getFloatExtra("productPrice", 0);
        int productQuantity = intent.getIntExtra("productQuantity", 0);

        displayActionBar();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dsOrderDetail = new ArrayList<>();
        adapterOrderDetail = new OrderDetailAdapterTest(RatingActivity.this, R.layout.item_rating, dsOrderDetail);

        eventsRating();
    }

    private void eventsRating() {
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String description = getDescriptionForRating(rating);
            binding.txtRatingDescription.setText(description);
        });

        binding.btnAddPhoto.setOnClickListener(v -> {
            if (imageUriList.size() < MAX_IMAGE_COUNT) {
                pickImageFromGallery();
            } else {
                Toast.makeText(RatingActivity.this, "Only maximum 3 images can be selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRating.setOnClickListener(v -> {
            float rating = binding.ratingBar.getRating();
            String comment = binding.edtReview.getText().toString();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Rating review = new Rating(userId, rating, comment);
            Intent intent = getIntent();
            String bookId = intent.getStringExtra("bookID");
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(bookId);

            uploadImagesToFirebase(review, bookRef);
        });
    }

    private void uploadImagesToFirebase(Rating review, DatabaseReference bookRef) {
        if (imageUriList.isEmpty()) {
            saveReviewToDatabase(review, bookRef, new ArrayList<>());
            return;
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        for (Uri imageUri : imageUriList) {
            String imageName = "imagesReview/" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = mStorageReference.child(imageName);
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrls.add(uri.toString());
                        if (imageUrls.size() == imageUriList.size()) {
                            saveReviewToDatabase(review, bookRef, imageUrls);
                        }
                    });
                } else {
                    Toast.makeText(RatingActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveReviewToDatabase(Rating review, DatabaseReference bookRef, ArrayList<String> imageUrls) {
        review.setImageUrls(imageUrls);
        bookRef.child("reviews").push().setValue(review);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ordersRef.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && order.getStatus().equals("Hoàn tất")) {
                        String bookId = getIntent().getStringExtra("bookID");
                        for (OrderBook book : order.getOrderBooks()) {
                            if (book.getId().equals(bookId)) {
                                book.setReview(true);
                                orderSnapshot.getRef().setValue(order);
                            }
                        }
                    }
                }
                Toast.makeText(RatingActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RatingActivity.this, MyReviewActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RatingActivity.this, "Failed to submit rating!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDescriptionForRating(float rating) {
        switch ((int) rating) {
            case 1: return "Bad";
            case 2: return "Poor";
            case 3: return "Fair";
            case 4: return "Good";
            case 5: return "Amazing";
            default: return "";
        }
    }

    private void pickImageFromGallery() {
        if (imageUriList.size() < MAX_IMAGE_COUNT) {
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
            if (imageUri != null && !imageUriList.contains(imageUri)) {
                imageUriList.add(imageUri);
                displaySelectedImages();
            }
        }
    }

    private void displaySelectedImages() {
        binding.layoutSelectedImages.removeAllViews();
        for (Uri imageUri : imageUriList) {
            View imageItemView = getLayoutInflater().inflate(R.layout.item_image_remove, null);
            ImageView imageView = imageItemView.findViewById(R.id.imageView);
            ImageView removeImageView = imageItemView.findViewById(R.id.removeImageView);

            imageView.setImageURI(imageUri);

            removeImageView.setOnClickListener(v -> {
                imageUriList.remove(imageUri);
                displaySelectedImages();
            });

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
                // Handle error if any
            }
        });
    }
}
