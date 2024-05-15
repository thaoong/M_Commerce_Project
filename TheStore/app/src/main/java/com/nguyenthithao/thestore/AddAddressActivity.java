package com.nguyenthithao.thestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenthithao.model.Address;
import com.nguyenthithao.thestore.databinding.ActivityAddAddressBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddAddressActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_EDIT_ADDRESS = 102;
    ActivityAddAddressBinding binding;
    List<Address> addressList = new ArrayList<>();
    private Address defaultAddress; // Variable to store the default address
    DatabaseReference addressRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_address);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        addressRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId);
        loadAddresses(); // Load user's addresses
        addEvents();
    }

    private void addEvents() {
        binding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void saveAddress() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String province = binding.edtProvince.getText().toString().trim();
        String district = binding.edtDistrict.getText().toString().trim();
        String ward = binding.edtWard.getText().toString().trim();
        String street = binding.edtStreet.getText().toString().trim();
        boolean isDefault = binding.chkDefaultAddress.isChecked();

        if (!name.isEmpty() && !phone.isEmpty() && !province.isEmpty() && !district.isEmpty() && !ward.isEmpty() && !street.isEmpty()) {
            Address address = new Address(name, phone, province, district, ward, street, isDefault);

            // If setting this address as default, remove default flag from other addresses
            if (isDefault) {
                for (Address existingAddress : addressList) {
                    existingAddress.setDefault(false);
                    // Update existing address in the database
                    addressRef.child(existingAddress.getAddressId()).setValue(existingAddress);
                }
                defaultAddress = address; // Update default address
            }

            String addressId = addressRef.push().getKey();
            address.setAddressId(addressId);
            addressRef.child(addressId).setValue(address);
            String title = getResources().getString(R.string.strSaveAddressSuccessfully);
            Toast.makeText(AddAddressActivity.this, title, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("address", (CharSequence) address);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            String title = getResources().getString(R.string.strFillAllFields);
            Toast.makeText(AddAddressActivity.this, title, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        String title = getResources().getString(R.string.strAddNewAddress);
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

    private void loadAddresses() {
        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    Address address = addressSnapshot.getValue(Address.class);
                    if (address != null) {
                        address.setAddressId(addressSnapshot.getKey());
                        addressList.add(address);
                        if (address.isDefault()) {
                            defaultAddress = address;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String title = getResources().getString(R.string.strFailedLoadAddresses);
                Toast.makeText(AddAddressActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
