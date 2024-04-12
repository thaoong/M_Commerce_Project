package com.nguyenthithao.thestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nguyenthithao.thestore.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_detail);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}