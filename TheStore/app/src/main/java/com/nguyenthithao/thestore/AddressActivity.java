package com.nguyenthithao.thestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nguyenthithao.model.Address;
import com.nguyenthithao.thestore.databinding.ActivityAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    ListView lvAddress;
    List<Address> addressList;
    ArrayAdapter<String> adapter;
    private static final int REQUEST_CODE_ADD_ADDRESS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();

        lvAddress = binding.lvAddress;
        addressList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        lvAddress.setAdapter(adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK && data != null) {

            Address address = (Address) data.getSerializableExtra("address");
            if (address != null) {
                addressList.add(address);
                updateListView();
            }
        }
    }

    private void updateListView() {
        List<String> addressStrings = new ArrayList<>();
        for (Address address : addressList) {
            String addressString = address.getName() + "\n" +
                    address.getPhone() + "\n" +
                    address.getStreet() + ", " +
                    address.getWard() + ", " +
                    address.getDistrict() + ", " +
                    address.getProvince();
            addressStrings.add(addressString);
        }
        adapter.clear();
        adapter.addAll(addressStrings);
        adapter.notifyDataSetChanged();
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
