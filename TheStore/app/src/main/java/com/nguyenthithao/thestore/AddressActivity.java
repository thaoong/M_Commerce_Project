package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.nguyenthithao.thestore.databinding.ActivityAddressBinding;

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_address);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>Sổ địa chỉ</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void processAddNewAddress(View view) {
        Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
        startActivity(intent);
    }
}