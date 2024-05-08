package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.SliderAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.CartItem;
import com.nguyenthithao.model.SliderItems;
import com.nguyenthithao.model.WishlistBook;
import com.nguyenthithao.thestore.databinding.ActivityProductDetailBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    private Book selectedBook;
    private Handler slideHandle = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference wishlistRef;
    private boolean isInWishlist = false;

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
        addEvents();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Lấy thông tin sách đã chọn
        selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");

        // Kiểm tra sách có trong wishlist hay không
        checkWishlistStatus();

        // Đăng ký sự kiện click cho btnAddWishlist
        binding.btnAddWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWishlist();
            }
        });
    }

    private void addEvents() {
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAddToCart();
            }
        });

        binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processBuyNow();
            }
        });
    }

    private void processBuyNow() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(ProductDetailActivity.this, "Please log in to continue shopping", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductDetailActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_cart_choose_quanntity, null);
            dialogBuilder.setView(dialogView);

            ImageView btnDecrease = dialogView.findViewById(R.id.btnDecrease);
            EditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
            ImageView btnIncrease = dialogView.findViewById(R.id.btnIncrease);
            Button btnAddToCart = dialogView.findViewById(R.id.btnAddToCart);
            btnAddToCart.setText("Buy now");
            final int[] currentQuantity = {1};
            edtQuantity.setText(String.valueOf(currentQuantity[0]));

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentQuantity[0] > 0) {
                        currentQuantity[0]--;
                        edtQuantity.setText(String.valueOf(currentQuantity[0]));
                    }
                }
            });

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuantity[0]++;
                    edtQuantity.setText(String.valueOf(currentQuantity[0]));
                }
            });

            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(edtQuantity.getText().toString());
                    ArrayList<CartItem> selectedCartItems = new ArrayList<>();
                    CartItem cartItem = new CartItem(selectedBook.getId(), selectedBook.getName(), selectedBook.getUnitPrice(), selectedBook.getImageLink().get(0), selectedBook.getOldPrice(), quantity);
                    selectedCartItems.add(cartItem);
                    if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
                        Intent intent = new Intent(ProductDetailActivity.this, PrePaymentActivity.class);
                        intent.putParcelableArrayListExtra("selectedItems", selectedCartItems);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void processAddToCart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(ProductDetailActivity.this, "Please log in to continue shopping", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String bookId = selectedBook.getId();
            DatabaseReference cartRef = database.getReference("carts").child(userId).child(bookId);

            String productName = selectedBook.getName();
            float unitPrice = selectedBook.getUnitPrice();
            float oldPrice = selectedBook.getOldPrice();
            String productImageUrl = selectedBook.getImageLink().get(0);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductDetailActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_cart_choose_quanntity, null);
            dialogBuilder.setView(dialogView);

            ImageView btnDecrease = dialogView.findViewById(R.id.btnDecrease);
            EditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
            ImageView btnIncrease = dialogView.findViewById(R.id.btnIncrease);
            Button btnAddToCart = dialogView.findViewById(R.id.btnAddToCart);

            final int[] currentQuantity = {1};
            edtQuantity.setText(String.valueOf(currentQuantity[0]));

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentQuantity[0] > 0) {
                        currentQuantity[0]--;
                        edtQuantity.setText(String.valueOf(currentQuantity[0]));
                    }
                }
            });

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuantity[0]++;
                    edtQuantity.setText(String.valueOf(currentQuantity[0]));
                }
            });

            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(edtQuantity.getText().toString());
                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Product already exists in the cart, update the quantity
                                CartItem cartItem = snapshot.getValue(CartItem.class);
                                int currentQuantity = cartItem.getQuantity();
                                int updatedQuantity = currentQuantity + quantity;
                                cartItem.setQuantity(updatedQuantity);
                                cartRef.setValue(cartItem);
                            } else {
                                // Product doesn't exist in the cart, add a new entry
                                CartItem cartItem = new CartItem(bookId, productName, unitPrice, productImageUrl, oldPrice, quantity);
                                cartRef.setValue(cartItem);
                            }
                            Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any database error
                        }
                    });
                }
            });
        }
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
    private void checkWishlistStatus() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String bookId = selectedBook.getId();
            wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists").child(userId).child("books").child(bookId);

            wishlistRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isInWishlist = snapshot.exists();
                    updateWishlistIcon();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        } else {
            binding.btnAddWishlist.setEnabled(false);
        }
    }
    private void updateWishlistIcon() {
        if (isInWishlist) {
            binding.btnAddWishlist.setImageResource(R.drawable.ic_favorite_24);
        } else {
            binding.btnAddWishlist.setImageResource(R.drawable.ic_favorite_border_24);
        }
    }
    private void toggleWishlist() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String bookId = selectedBook.getId();
            DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists").child(userId).child("books").child(bookId);

            if (isInWishlist) {
                // Xóa sách khỏi wishlist
                wishlistRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                isInWishlist = false;
                                updateWishlistIcon();
                                Toast.makeText(ProductDetailActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Failed to remove from wishlist", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Thêm sách vào wishlist
                selectedBook = (Book) getIntent().getSerializableExtra("SELECTED_BOOK");
                String productName = selectedBook.getName();
                float unitPrice = selectedBook.getUnitPrice();
                float oldPrice = selectedBook.getOldPrice();
                String productImageUrl = selectedBook.getImageLink().get(0);
                String author = selectedBook.getAuthor();
                String description = selectedBook.getDescription();
                String category = selectedBook.getCategory();
                String publicationDate = selectedBook.getPublicationDate();
                float rating = selectedBook.getRating();
                int reviewNum = selectedBook.getReviewNum();
                int bestSelling = selectedBook.getBestSelling();

                WishlistBook wishlistBook = new WishlistBook(bookId, productName, author, description, productImageUrl, category, publicationDate, rating, reviewNum, unitPrice, oldPrice, bestSelling);

                wishlistRef.setValue(wishlistBook)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                isInWishlist = true;
                                updateWishlistIcon();
                                Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(ProductDetailActivity.this, "Please log in to use wishlist", Toast.LENGTH_SHORT).show();
        }
    }
}