package com.nguyenthithao.thestore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenthithao.thestore.databinding.FragmentHomeBinding;
import com.nguyenthithao.thestore.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    private View view;
    FragmentNotificationBinding binding;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        addEvents();
        return view;
    }

    private void addEvents() {
    }
}