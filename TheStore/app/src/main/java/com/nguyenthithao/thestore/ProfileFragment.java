package com.nguyenthithao.thestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nguyenthithao.thestore.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private View view;
    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        addEvents();
        return view;
    }

    private void addEvents() {
        binding.btnModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        binding.btnModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
    }
}