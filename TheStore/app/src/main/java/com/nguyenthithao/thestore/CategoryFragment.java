package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.adapter.BookAdapter;
import com.nguyenthithao.adapter.CategoryAdapter;
import com.nguyenthithao.model.Book;
import com.nguyenthithao.model.Category;
import com.nguyenthithao.thestore.databinding.FragmentCategoryBinding;
import com.nguyenthithao.thestore.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    private View view;
    FragmentCategoryBinding binding;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        loadCategories();
        return view;
    }

    private void loadCategories() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("categories");
        ArrayList<Category> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Category.class));
                    }
                    if (!items.isEmpty()) {
                        binding.rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        binding.rvCategory.setAdapter(new CategoryAdapter(items));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}