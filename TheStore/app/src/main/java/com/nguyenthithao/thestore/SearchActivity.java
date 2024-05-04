package com.nguyenthithao.thestore;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookRecommendAdapter;
import com.nguyenthithao.adapter.CategoryAdapter;
import com.nguyenthithao.adapter.RecentSearchesAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    private RecyclerView recyclerViewRecentSearches;
    private RecentSearchesAdapter recentSearchesAdapter;
    private ArrayList<String> recentSearches = new ArrayList<>();
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.edtSearch.requestFocus();
        hideActionBar();
        addEvents();
        loadCategory();
        loadRecommendation();
        setupRecyclerView();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadRecentSearchesFromFirebase();
        } else {
            loadRecentSearchesFromSharedPreferences();
        }
        Intent intent = getIntent();
        if (intent.hasExtra("QUERY")) {
            String query = intent.getStringExtra("QUERY");
            binding.edtSearch.setText(query);
            binding.edtSearch.setSelection(query.length());
        }
        binding.txtClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRecentSearches();
            }
        });
//        binding.textViewCate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        binding.edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtSearch.getRight() - binding.edtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        binding.edtSearch.setText("");
                        binding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_image_search_24, 0);
                        return true;
                    }
                }
                return false;
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_24, 0);
                } else {
                    binding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_image_search_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadRecommendation() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("books");
        binding.progressBarRec.setVisibility(View.VISIBLE);
        ArrayList<Book> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Book book = issue.getValue(Book.class);
                        if (book != null && book.getBestSelling() == 1) {
                            items.add(book);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.rvRecommend.setLayoutManager(new LinearLayoutManager(SearchActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvRecommend.setAdapter(new BookRecommendAdapter(items));
                    }
                    binding.progressBarRec.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearRecentSearches() {
        recentSearches.clear();
        recentSearchesAdapter.notifyDataSetChanged();

        if (currentUser != null) {
            clearRecentSearchesFromFirebase();
        } else {
            clearRecentSearchesFromSharedPreferences();
        }
    }

    private void clearRecentSearchesFromFirebase() {
        String userId = currentUser.getUid();
        DatabaseReference recentSearchesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("recentSearches");
        recentSearchesRef.removeValue();
    }

    private void clearRecentSearchesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("recentSearches");
        editor.apply();
    }

    private void hideActionBar() {
        getSupportActionBar().hide();
    }

    private void addEvents() {
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.edtSearch.getText().toString();
                if (query.isEmpty()) {
                    showSearchTermWarning();
                } else {
                    performSearch(query);
                    addRecentSearch(query);
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        binding.btnEmptySearchBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.edtSearch.setText("");
//            }
//        });
    }

    private void showSearchTermWarning() {
        Toast.makeText(this, "Please input your search term", Toast.LENGTH_SHORT).show();

    }

    private void setupRecyclerView() {
        recyclerViewRecentSearches = binding.recentsearches;
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        recyclerViewRecentSearches.setLayoutManager(layoutManager);

        recentSearchesAdapter = new RecentSearchesAdapter(recentSearches);
        recyclerViewRecentSearches.setAdapter(recentSearchesAdapter);

        recentSearchesAdapter.setOnItemClickListener(new RecentSearchesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String recentSearch) {
                binding.edtSearch.setText(recentSearch);
                binding.edtSearch.setSelection(recentSearch.length());
            }
        });
    }

    private void performSearch(String query) {
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("QUERY", binding.edtSearch.getText().toString());
        startActivity(intent);
    }

    private void addRecentSearch(String query) {
        if (!recentSearches.contains(query)) {
            recentSearches.add(0, query);

            // Limit the recentSearches list to 10 items
            if (recentSearches.size() > 10) {
                recentSearches.remove(recentSearches.size() - 1);
            }

            recentSearchesAdapter.notifyDataSetChanged();

            if (currentUser != null) {
                saveRecentSearchesToFirebase();
            } else {
                saveRecentSearchesToSharedPreferences();
            }
        } else {
            // Move the query to the top of the list
            recentSearches.remove(query);
            recentSearches.add(0, query);
            recentSearchesAdapter.notifyDataSetChanged();
        }
    }

    private void saveRecentSearchesToFirebase() {
        String userId = currentUser.getUid();
        DatabaseReference recentSearchesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("recentSearches");
        recentSearchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String search = snapshot.getValue(String.class);
                    if (search.equals(recentSearches.get(0))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    recentSearchesRef.push().setValue(recentSearches.get(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void loadRecentSearchesFromFirebase() {
        String userId = currentUser.getUid();
        DatabaseReference recentSearchesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("recentSearches");
        recentSearchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentSearches.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String search = snapshot.getValue(String.class);
                    recentSearches.add(search);
                }
                recentSearchesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void saveRecentSearchesToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("recentSearches", new HashSet<>(recentSearches));
        editor.apply();
    }

    private void loadRecentSearchesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> savedRecentSearches = sharedPreferences.getStringSet("recentSearches", new HashSet<>());
        recentSearches.addAll(savedRecentSearches);
        recentSearchesAdapter.notifyDataSetChanged();
    }
    private void loadCategory() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("categories");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Category.class));
                    }
                    if (!items.isEmpty()) {
                        binding.rvCategory.setLayoutManager(new LinearLayoutManager(SearchActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.rvCategory.setAdapter(new CategoryAdapter(items));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}