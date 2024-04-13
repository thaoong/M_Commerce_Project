package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.nguyenthithao.thestore.databinding.ActivityMyReviewBinding;

import java.util.Locale;

public class MyReviewActivity extends AppCompatActivity {
    ActivityMyReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_change_language);
        binding = ActivityMyReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strMyReview);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>"+title+"</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}