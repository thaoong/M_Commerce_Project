package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenthithao.thestore.databinding.FragmentCategoryBinding;
import com.nguyenthithao.thestore.databinding.FragmentProfileBinding;

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
        addEvents();
        return view;
    }

    private void addEvents() {
    }
}